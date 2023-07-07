package com.romkapo.todoapp.data.repository

import com.romkapo.todoapp.core.DeviceId
import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.UnSyncAction
import com.romkapo.todoapp.data.model.UnSyncAction.ADD
import com.romkapo.todoapp.data.model.UnSyncAction.DELETE
import com.romkapo.todoapp.data.model.UnSyncAction.EDIT
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

    private val _stateRequest = MutableStateFlow<Resource>(Resource.Success(""))
    override val stateRequest get() = _stateRequest.asStateFlow()

    private val deviceId = DeviceId().id

    override fun getTodoList(): Flow<List<TodoItem>> {
        return toDoItemDao.getTodoListFlow()
    }

    override fun getTodoItem(id: String): TodoItem? {
        return toDoItemDao.getTodoItemById(id)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        safeCallTodo(todoItem, ADD)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        safeCallTodo(todoItem, EDIT)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        safeCallTodo(todoItem, DELETE)
    }

    override suspend fun fetchTasks(): Resource {
        return safeCallList()
    }

    private suspend fun safeCallTodo(
        syncItem: TodoItem,
        type: UnSyncAction,
    ) {
        try {
            val resultApi = when (type) {
                ADD -> addTodoItemSafe(syncItem)
                EDIT -> updateTodoItemSafe(syncItem)
                DELETE -> deleteTodoItemSafe(syncItem)
            }
            if (resultApi.isSuccessful) {
                appSharedPreferences.putRevisionId(resultApi.body()!!.revision)
            } else {
                resultCodeCallback(resultApi.code())
                failurePush(syncItem, type)
            }
        } catch (exception: Exception) {
            _stateRequest.value = Resource.Error("Неизвестная ошибка, пробуем снова")
            failurePush(syncItem, type)
        }
    }

    private suspend fun safeCallList(): Resource {
        _stateRequest.value = Resource.Loading("Loading")
        try {
            val resultApi = todoAPI.getList()
            if (resultApi.isSuccessful) {
                return mergeData(resultApi.body()!!)
            }
        } catch (exception: Exception) {
            _stateRequest.value = Resource.Error("Ошибка при обновлении")
        }
        return Resource.Error("Ошибка при обновлении")
    }


    private fun resultCodeCallback(resultCode: Int) {
        when (resultCode) {
            400 -> _stateRequest.value = Resource.Error("Проблема с клиентом, пробуем снова")

            401 -> _stateRequest.value = Resource.Error("Проблема с авторизацией, пробуем снова")

            404 -> _stateRequest.value = Resource.Error("Элемента нет на сервере, пробуем снова")

            in 500..600 -> _stateRequest.value = Resource.Error("Проблема с сетью, пробуем снова")
        }
    }

    private suspend fun failurePush(syncItem: TodoItem, type: UnSyncAction) {
        val operation = syncItem.toOperationItem(type.label)
        if (type == DELETE) {
            todoOperationDAO.deleteOperationWithId(operation.id)
        }
        todoOperationDAO.insertUnSyncOperation(operation)
        fetchTasks()
    }

    private suspend inline fun addTodoItemSafe(todoItem: TodoItem): Response<TodoItemResponse> {
        toDoItemDao.upsertTodoItem(todoItem)
        return todoAPI.addItem(
            appSharedPreferences.getRevisionId(),
            TodoItemRequest("ok", todoItem.toNetworkItem(deviceId)),
        )
    }

    private suspend fun updateTodoItemSafe(todoItem: TodoItem): Response<TodoItemResponse> {
        toDoItemDao.upsertTodoItem(todoItem)
        return todoAPI.updateItem(
            appSharedPreferences.getRevisionId(),
            todoItem.id,
            TodoItemRequest(
                "ok",
                todoItem.toNetworkItem(deviceId),
            ),
        )
    }

    private suspend fun deleteTodoItemSafe(todoItem: TodoItem): Response<TodoItemResponse> {
        toDoItemDao.deleteTodoItem(todoItem)
        return todoAPI.deleteItem(appSharedPreferences.getRevisionId(), todoItem.id)
    }


    override suspend fun updateRemoteTasks(mergedList: List<ApiTodoItem>): Resource {
        val response = todoAPI.updateList(
            revision = appSharedPreferences.getRevisionId(),
            body = TodoItemListRequest(status = "ok", mergedList),
        )

        if (response.isSuccessful) {
            appSharedPreferences.putRevisionId(response.body()!!.revision)
            todoOperationDAO.dropTodoItems()
            return Resource.Success("Успешно обновлено")
        }
        return Resource.Error("Ошибка при отправлении")
    }

    private suspend fun mergeData(body: TodoItemListResponse): Resource {
        val revision = body.revision
        val networkList = body.list.map { it.toTodoItem() }
        val unSyncOperationsList = todoOperationDAO.getUnSyncTodoList()
        val mergedList = mutableMapOf<String, TodoItem>()

        for (item in networkList) {
            mergedList[item.id] = item
        }
        for (operation in unSyncOperationsList) {
            val localItemId = operation.id
            when (operation.type) {
                ADD.label -> mergedList[localItemId] =
                    toDoItemDao.getTodoItemById(localItemId)!!

                EDIT.label -> mergedList[localItemId]?.let { netItem ->
                    mergedList[localItemId] = editTodoUpdate(operation, netItem)
                }

                DELETE.label -> mergedList.remove(localItemId)
            }
        }

        appSharedPreferences.putRevisionId(revision)
        val merged = mergedList.values.toList().filter { it.id != "-1" }
        toDoItemDao.dropTodoItems()
        toDoItemDao.insertTodoList(merged)
        return updateRemoteTasks(merged.map { it.toNetworkItem(deviceId) })
    }

    private fun editTodoUpdate(operation: UnSyncTodoItem, netItem: TodoItem): TodoItem {
        return if (!(netItem.dateEdit != null && netItem.dateEdit!! >= operation.timestamp)) {
            toDoItemDao.getTodoItemById(operation.id)!!
        }else{
            netItem
        }
    }
}
