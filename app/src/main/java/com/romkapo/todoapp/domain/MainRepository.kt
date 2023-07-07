package com.romkapo.todoapp.domain

import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.ApiTodoItem
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getTodoList(): Flow<List<TodoItem>>

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)
    fun getTodoItem(id: String): TodoItem?


    suspend fun updateRemoteTasks(mergedList: List<ApiTodoItem>): Resource<Any>

    suspend fun updateTask(): Boolean

}