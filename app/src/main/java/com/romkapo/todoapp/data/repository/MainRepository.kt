package com.romkapo.todoapp.data.repository

import android.util.Log
import com.romkapo.todoapp.core.DeviceId
import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.ApiTodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.model.network.TodoItemListRequest
import com.romkapo.todoapp.data.model.network.TodoItemRequest
import com.romkapo.todoapp.data.model.network.toNetworkItem
import com.romkapo.todoapp.data.model.network.toTodoItem
import com.romkapo.todoapp.data.network.TodoAPI
import com.romkapo.todoapp.data.room.TodoDAO
import com.romkapo.todoapp.data.room.TodoOperationDAO
import com.romkapo.todoapp.domain.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val toDoItemDao: TodoDAO,
    private val todoOperationDAO: TodoOperationDAO,
    private val todoAPI: TodoAPI,
    private val appSharedPreferences: AppSharedPreferences
) : MainRepository {

    private val deviceId = DeviceId().id

    override fun getTodoList(): Flow<List<TodoItem>> {
        return toDoItemDao.getTodoListFlow()
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
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

    override suspend fun updateTodoItem(todoItem: TodoItem) {
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

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
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

    override fun getTodoItem(id: String): TodoItem? {
        return toDoItemDao.getTodoItemById(id)
    }


    override suspend fun updateRemoteTasks(mergedList: List<ApiTodoItem>): Resource<Any> {
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

    override suspend fun updateTask(): Boolean {
        try {
            val networkListResponse = todoAPI.getList()

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision
                    val networkList = body.list.map { it.toTodoItem() }
                    val unSyncOperationsList = todoOperationDAO.getUnSyncTodoList()
                    val mergedList = HashMap<String, TodoItem>()

                    for (item in networkList) {
                        mergedList[item.id] = item
                    }
                    for (operation in unSyncOperationsList) {
                        val localItemId = operation.id
                        when (operation.type) {
                            "add" -> mergedList[localItemId] =
                                toDoItemDao.getTodoItemById(localItemId)!!

                            "edit" -> mergedList[localItemId]?.let { netItem ->
                                if (!(netItem.dateEdit != null && netItem.dateEdit!! >= operation.timestamp)) {
                                    mergedList[localItemId] =
                                        toDoItemDao.getTodoItemById(localItemId)!!
                                }
                            }

                            "drop" -> mergedList.remove(localItemId)
                        }
                    }

                    appSharedPreferences.putRevisionId(revision)
                    val merged = mergedList.values.toList()
                    toDoItemDao.insertTodoList(merged)
                    updateRemoteTasks(merged.map { it.toNetworkItem(deviceId) })
                    return true
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