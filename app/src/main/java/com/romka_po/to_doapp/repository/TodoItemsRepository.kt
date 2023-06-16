package com.romka_po.to_doapp.repository

import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.utils.Importance
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoItemsRepository @Inject constructor() {
    private var todoItems = listOf<TodoItem>()

    fun addData(item: TodoItem, position: Int = todoItems.size) {
        val list = todoItems.toMutableList()
        list.add(position, item)
        todoItems = list
    }

    fun getFlowOfData(): Flow<List<TodoItem>> = flow {
        emit(todoItems)
    }

    fun removeTodoItemWithID(id: String) {
        val list = todoItems.toMutableList()
        for (i in 0 until todoItems.size) {
            if (list[i].id == id) {
                list.removeAt(i)
                break
            }
        }
        todoItems = list
    }

    fun editTodoItem(todoItem: TodoItem) {
        val list = todoItems.toMutableList()
        for (i in 0 until todoItems.size) {
            if (list[i].id == todoItem.id) {
                list[i] = todoItem
                break
            }
        }
        todoItems = list
    }

    init {
        val list = todoItems.toMutableList()
        for (i in 1..20) {
            val todoItem = TodoItem(
                id = "ID$i",
                text = "Todo ${i.toString().repeat(i * i)}",
                importance = when {
                    i <= 5 -> Importance.LOW
                    i <= 15 -> Importance.MEDIUM
                    else -> Importance.HIGH
                },
                isComplete = false,
                dateCreate = System.currentTimeMillis(),
                dateComplete = null,
                dateEdit = null
            )
            list.add(todoItem)
        }
        todoItems = list
    }


}