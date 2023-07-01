package com.romkapo.todoapp.data.repository

import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.romkapo.todoapp.core.DeviceId
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.ApiTodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.model.network.TodoItemListRequest
import com.romkapo.todoapp.data.model.network.TodoItemRequest
import com.romkapo.todoapp.data.model.network.TodoItemResponse
import com.romkapo.todoapp.data.model.network.toNetworkItem
import com.romkapo.todoapp.data.model.network.toTodoItem
import com.romkapo.todoapp.data.network.Resource
import com.romkapo.todoapp.data.network.TodoAPI
import com.romkapo.todoapp.data.room.TodoDAO
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val toDoItemDao: TodoDAO,
    private val remoteDataSource: TodoAPI,
    private val appSharedPreferences: AppSharedPreferences
) {

    private val deviceId = DeviceId().id

    fun getAllToDoItems(): Flow<List<TodoItem>> {
        return toDoItemDao.getTodoListFlow()
    }
    fun getUncheckedToDoItems(): Flow<List<TodoItem>> {
        return toDoItemDao.getUncheckedTodoListFlow()
    }

    fun getToDoItemById(id: String): TodoItem? {
        return toDoItemDao.getTodoItemById(id = id)
    }

    suspend fun updateStatusToDoItem(id: String, done: Boolean) {
        return toDoItemDao.setTodoItemState(id, done, System.currentTimeMillis())
    }

    suspend fun updateToDoItem(toDoItem: TodoItem) {
        return toDoItemDao.insertTodoItem(toDoItem)
    }

    suspend fun createItem(toDoItem: TodoItem) {
        return toDoItemDao.insertTodoItem(toDoItem)
    }

    suspend fun deleteToDoItem(id: String) {
        return toDoItemDao.deleteTodoItemById(id)
    }

    suspend fun updateRemoteTask(toDoTask: TodoItem) {
        try {
            val response = remoteDataSource.updateItem(
                revision = appSharedPreferences.getRevisionId(),
                itemId = toDoTask.id,
                TodoItemRequest(
                    "ok",
                    toDoTask.toNetworkItem(deviceId)
                )
            )

            checkResponse(response)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
        }


    }

    suspend fun deleteRemoteTask(taskId: String) {
        try {
            val response = remoteDataSource.deleteItem(
                revision = appSharedPreferences.getRevisionId(),
                itemId = taskId
            )
            checkResponse(response)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }

    }

    suspend fun createRemoteTask(newTask: TodoItem) {
        try {
            val response = remoteDataSource.addItem(
                revision = appSharedPreferences.getRevisionId(),
                TodoItemRequest(
                    "ok",
                    newTask.toNetworkItem(deviceId)
                )
            )

            checkResponse(response)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }
    }

    private suspend fun updateRemoteTasks(mergedList: List<ApiTodoItem>): Resource<Any> {
        try {
            val response = remoteDataSource.updateList(
                revision = appSharedPreferences.getRevisionId(),
                TodoItemListRequest(status = "ok", mergedList)
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    appSharedPreferences.putRevisionId(responseBody.revision)
                    toDoItemDao.insertTodoList(responseBody.list.map {
                        it.toTodoItem()
                    })
                    return Resource.Success(responseBody.list)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            e.message?.let { Log.d("TYT", it) }
            return Resource.Error("Merge failed, continue offline.")
        }
        return Resource.Error("Merge failed, continue offline.")
    }

    suspend fun getRemoteTasks(): Resource<Any> {
        try {
            val networkListResponse = remoteDataSource.getList()

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision
                    val networkList = body.list
                    val currentList = toDoItemDao.getTodoList().map {
                        it.toNetworkItem(
                            deviceId
                        )
                    }
                    val mergedList = HashMap<String, ApiTodoItem>()

                    for (item in currentList) {
                        mergedList[item.id] = item
                    }
                    for (item in networkList) {
                        if (mergedList.containsKey(item.id)) {
                            val item1 = mergedList[item.id]
                            item.changedAt?.let {
                                if (!(item1!!.changedAt != null && item1.changedAt!! >= item.changedAt)) {
                                    mergedList[item.id] = item
                                }
                            }
                        } else if (revision != appSharedPreferences.getRevisionId()) {
                            mergedList[item.id] = item
                        }
                    }

                    return updateRemoteTasks(mergedList.values.toList())
                }
            }

        } catch (e: Exception) {
            e.message?.let { Log.d("TYT", it) }
            return Resource.Error("Merge failed, continue offline.")
        }
        return Resource.Error("Merge failed, continue offline.")
    }


    private fun checkResponse(response: Response<TodoItemResponse>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                appSharedPreferences.putRevisionId(responseBody.revision)
            }
        } else {
            response.errorBody()?.close()
        }
    }
}