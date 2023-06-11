package com.romka_po.to_doapp.repository

import com.romka_po.to_doapp.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class TodoItemsRepository {
    val todoItemList:List<TodoItem> = mutableListOf<TodoItem>()

    fun getTodoItems()= flow {
        emit(todoItemList)
    }


}