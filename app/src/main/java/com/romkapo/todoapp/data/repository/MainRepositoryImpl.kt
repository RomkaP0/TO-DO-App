package com.romkapo.todoapp.data.repository

import com.romkapo.todoapp.core.DeviceId
import com.romkapo.todoapp.data.model.BadRequestException
import com.romkapo.todoapp.data.model.ClientSideException
import com.romkapo.todoapp.data.model.ItemNotFoundException
import com.romkapo.todoapp.data.model.NetworkException
import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.data.model.SyncFailedException
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.UnSyncAction
import com.romkapo.todoapp.data.model.UpdateFailedException
import com.romkapo.todoapp.data.model.network.ApiTodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.model.network.TodoItemListRequest
import com.romkapo.todoapp.data.model.network.TodoItemListResponse
import com.romkapo.todoapp.data.model.network.TodoItemRequest
import com.romkapo.todoapp.data.model.network.TodoItemResponse
import com.romkapo.todoapp.data.model.network.UnSyncTodoItem
import com.romkapo.todoapp.data.model.network.toNetworkItem
import com.romkapo.todoapp.data.model.network.toOperationItem
import com.romkapo.todoapp.data.model.network.toTodoItem
import com.romkapo.todoapp.data.network.TodoAPI
import com.romkapo.todoapp.data.room.TodoDAO
import com.romkapo.todoapp.data.room.TodoOperationDAO
import com.romkapo.todoapp.domain.MainRepository
import com.romkapo.todoapp.utils.Constants.CLIENT_EXCEPTION
import com.romkapo.todoapp.utils.Constants.NET_EXCEPTION_DOWN
import com.romkapo.todoapp.utils.Constants.NET_EXCEPTION_UP
import com.romkapo.todoapp.utils.Constants.NOT_FOUND_EXCEPTION
import com.romkapo.todoapp.utils.Constants.OK
import com.romkapo.todoapp.utils.Constants.SYNC_EXCEPTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val toDoItemDao: TodoDAO,
    private val todoOperationDAO: TodoOperationDAO,
    private val todoAPI: TodoAPI,
    private val appSharedPreferences: AppSharedPreferences,
) : MainRepository {
    private val _stateRequest = MutableStateFlow<Resource>(Resource.Success)
    override val stateRequest get() = _stateRequest.asStateFlow()

    private val deviceId = DeviceId().id

    override fun getTodoList(): Flow<List<TodoItem>> {
        return toDoItemDao.getTodoListFlow()
    }

    override fun getTodoItem(id: String): TodoItem? {
        return toDoItemDao.getTodoItemById(id)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        safeCallTodo(todoItem, UnSyncAction.ADD)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        safeCallTodo(todoItem, UnSyncAction.EDIT)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        safeCallTodo(todoItem, UnSyncAction.DELETE)
    }

    override suspend fun fetchTasks(): Resource {
        return safeCallList()
    }

    private suspend fun safeCallTodo(syncItem: TodoItem, type: UnSyncAction) {
        try {
            val resultApi = when (type) {
                UnSyncAction.ADD -> addTodoItemSafe(syncItem)
                UnSyncAction.EDIT -> updateTodoItemSafe(syncItem)
                UnSyncAction.DELETE -> deleteTodoItemSafe(syncItem)
            }

            if (resultApi.isSuccessful) {
                appSharedPreferences.putRevisionId(resultApi.body()!!.revision)
            } else {
                resultCodeCallback(resultApi.code())
                failurePush(syncItem, type)
            }
        } catch (exception: Exception) {
            _stateRequest.value = Resource.Error(NetworkException)
            failurePush(syncItem, type)
        }
    }

    private suspend fun safeCallList(): Resource {
        try {
            val resultApi = todoAPI.getList()
            if (resultApi.isSuccessful) {
                return mergeData(resultApi.body()!!)
            }
        } catch (exception: Exception) {
            _stateRequest.value = Resource.Error(UpdateFailedException)
        }
        return Resource.Error(UpdateFailedException)
    }

    private fun resultCodeCallback(resultCode: Int) {
        when (resultCode) {
            CLIENT_EXCEPTION -> _stateRequest.value = Resource.Error(ClientSideException)
            SYNC_EXCEPTION -> _stateRequest.value = Resource.Error(SyncFailedException)
            NOT_FOUND_EXCEPTION -> _stateRequest.value = Resource.Error(ItemNotFoundException)
            in NET_EXCEPTION_DOWN..NET_EXCEPTION_UP -> _stateRequest.value =
                Resource.Error(BadRequestException)
        }
    }

    private suspend fun failurePush(syncItem: TodoItem, type: UnSyncAction) {
        val operation = syncItem.toOperationItem(type.label)
        if (type == UnSyncAction.DELETE) {
            todoOperationDAO.deleteOperationWithId(operation.id)
        }
        todoOperationDAO.insertUnSyncOperation(operation)
        fetchTasks()
    }

    private suspend inline fun addTodoItemSafe(todoItem: TodoItem): Response<TodoItemResponse> {
        toDoItemDao.upsertTodoItem(todoItem)
        return todoAPI.addItem(
            appSharedPreferences.getRevisionId(),
            TodoItemRequest(OK, todoItem.toNetworkItem(deviceId)),
        )
    }

    private suspend fun updateTodoItemSafe(todoItem: TodoItem): Response<TodoItemResponse> {
        toDoItemDao.upsertTodoItem(todoItem)
        return todoAPI.updateItem(
            appSharedPreferences.getRevisionId(),
            todoItem.id,
            TodoItemRequest(OK, todoItem.toNetworkItem(deviceId))
        )
    }

    private suspend fun deleteTodoItemSafe(todoItem: TodoItem): Response<TodoItemResponse> {
        toDoItemDao.deleteTodoItem(todoItem)
        return todoAPI.deleteItem(appSharedPreferences.getRevisionId(), todoItem.id)
    }

    override suspend fun updateRemoteTasks(mergedList: List<ApiTodoItem>): Resource {
        val response = todoAPI.updateList(
            revision = appSharedPreferences.getRevisionId(),
            body = TodoItemListRequest(status = OK, mergedList),
        )

        if (response.isSuccessful) {
            appSharedPreferences.putRevisionId(response.body()!!.revision)
            todoOperationDAO.dropTodoItems()
            return Resource.Success
        }
        return Resource.Error(UpdateFailedException)
    }

    private suspend fun mergeData(body: TodoItemListResponse): Resource {
        val revision = body.revision
        val mergedList = body.list.associate { it.id to it.toTodoItem() }.toMutableMap()

        for (operation in todoOperationDAO.getUnSyncTodoList()) {
            val localItemId = operation.id

            when (operation.type) {
                UnSyncAction.ADD.label -> mergedList[localItemId] =
                    toDoItemDao.getTodoItemById(localItemId)!!
                UnSyncAction.EDIT.label -> mergedList[localItemId]?.let { netItem ->
                    mergedList[localItemId] = editTodoUpdate(operation, netItem)
                }
                UnSyncAction.DELETE.label -> mergedList.remove(localItemId)
            }
        }

        appSharedPreferences.putRevisionId(revision)

        val merged = mergedList.values.toList()
        toDoItemDao.dropTodoItems()
        toDoItemDao.insertTodoList(merged)

        return updateRemoteTasks(merged.map { it.toNetworkItem(deviceId) })
    }

    private fun editTodoUpdate(operation: UnSyncTodoItem, netItem: TodoItem): TodoItem {
        return if (!(netItem.dateEdit != null && netItem.dateEdit!! >= operation.timestamp)) {
            toDoItemDao.getTodoItemById(operation.id)!!
        } else {
            netItem
        }
    }
}
