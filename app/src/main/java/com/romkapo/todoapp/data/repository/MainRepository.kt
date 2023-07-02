package com.romkapo.todoapp.data.repository

import android.util.Log
import com.romkapo.todoapp.core.DeviceId
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.ApiTodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.model.network.TodoItemListRequest
import com.romkapo.todoapp.data.model.network.TodoItemRequest
import com.romkapo.todoapp.data.model.network.toNetworkItem
import com.romkapo.todoapp.data.model.network.toTodoItem
import com.romkapo.todoapp.data.network.Resource
import com.romkapo.todoapp.data.network.TodoAPI
import com.romkapo.todoapp.data.room.TodoDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val toDoItemDao: TodoDAO,
    private val todoAPI: TodoAPI,
    private val appSharedPreferences: AppSharedPreferences
) {

    private val deviceId = DeviceId().id

    fun getTodoList(): Flow<List<TodoItem>> {
        return toDoItemDao.getTodoListFlow()
    }

    suspend fun mergeTodoItemList() {
        try {
            val convertedLocalTodoList = toDoItemDao.getTodoListFlow().first().map {
                it.toNetworkItem(deviceId)
            }

            val resultApi =
                todoAPI.updateList(
                    appSharedPreferences.getRevisionId(),
                    TodoItemListRequest("ok", convertedLocalTodoList)
                )
            if (resultApi.isSuccessful) {
                appSharedPreferences.putRevisionId(resultApi.body()?.revision!!)
            }
        } catch (exception: Exception) {
            Log.i("mergeTodoItemList", exception.message.toString())
        }
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        try {
            toDoItemDao.upsertTodoItem(todoItem)
            val resultApi = todoAPI.addItem(
                appSharedPreferences.getRevisionId(),
                TodoItemRequest("ok", todoItem.toNetworkItem(deviceId))
            )
            if (resultApi.isSuccessful) {
                appSharedPreferences.putRevisionId(resultApi.body()?.revision!!)
            }
        } catch (exception: Exception) {
            Log.i("addTodoItem", exception.message.toString())
        }
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        try {
            toDoItemDao.upsertTodoItem(todoItem)
            val resultApi = todoAPI.updateItem(
                appSharedPreferences.getRevisionId(),
                todoItem.id,
                TodoItemRequest(
                    "ok",
                    todoItem.toNetworkItem(deviceId)
                )
            )

            if (resultApi.isSuccessful) {
                appSharedPreferences.putRevisionId(resultApi.body()?.revision!!)
            }
        } catch (exception: Exception) {
            Log.i("updateTodoItem", exception.message.toString())
        }
    }

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        try {
            toDoItemDao.deleteTodoItem(todoItem)
            val resultApi = todoAPI.deleteItem(appSharedPreferences.getRevisionId(), todoItem.id)
            if (resultApi.isSuccessful) {
                appSharedPreferences.putRevisionId(resultApi.body()?.revision!!)
            }
        } catch (exception: Exception) {
            Log.i("deleteTodoItem", exception.message.toString())
        }
    }

    fun getTodoItem(id: String): TodoItem? {
        return toDoItemDao.getTodoItemById(id)
    }


    private suspend fun updateRemoteTasks(mergedList: List<ApiTodoItem>): Resource<Any> {
        try {
            val response = todoAPI.updateList(
                revision = appSharedPreferences.getRevisionId(),
                body = TodoItemListRequest(status = "ok", mergedList)
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    appSharedPreferences.putRevisionId(responseBody.revision)

                    return Resource.Success(responseBody.list)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            return Resource.Error("Merge failed, continue offline.")
        }

        return Resource.Error("Merge failed, continue offline.")
    }

    suspend fun updateTask(): Boolean {
        try {
            val networkListResponse = todoAPI.getList()

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision
                    val networkList = body.list
                    val currentList = toDoItemDao.getTodoList().map {
                        it.toNetworkItem(deviceId)
                    }
                    val mergedList = HashMap<String, ApiTodoItem>()

                    for (item in currentList) {
                        mergedList[item.id] = item
                    }
                    for (item in networkList) {
                        if (mergedList.containsKey(item.id)) {
                            val item1 = mergedList[item.id]
                            item1!!.changedAt?.let {
                                if (item1.changedAt!! < item.changedAt!!) {
                                    mergedList[item.id] = item
                                }
                            }
                        } else if (revision != appSharedPreferences.getRevisionId()) {
                            mergedList[item.id] = item
                        }
                    }
                    appSharedPreferences.putRevisionId(revision)
                    val merged = mergedList.values.toList()
                    toDoItemDao.insertTodoList(merged.map { it.toTodoItem() })
                    updateRemoteTasks(merged)
                }
            } else {
                networkListResponse.errorBody()?.close()
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

}