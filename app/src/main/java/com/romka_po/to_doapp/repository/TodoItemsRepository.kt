package com.romka_po.to_doapp.repository

import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.utils.Importance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TodoItemsRepository @Inject constructor() {
    private var _todoItems = mutableListOf<TodoItem>()
    private val _todoItemsHolder = MutableStateFlow(listOf<TodoItem>())

    //this object sends out the immutable list
    val todoItems = _todoItemsHolder.asStateFlow()

    fun addData(item: TodoItem, position: Int = _todoItems.size) {
        _todoItems.add(position, item)
        val nl = _todoItems
        _todoItemsHolder.value = nl
    }

    fun removeTodoItemWithID(id: String) {
        for (i in 0 until _todoItems.size) {
            if (_todoItems[i].id == id) {
                _todoItems.removeAt(i)
                break
            }
        }
        val nl = _todoItems
        _todoItemsHolder.value = nl
    }

    fun editTodoItem(todoItem: TodoItem) {
        for (i in 0 until _todoItems.size) {
            if (_todoItems[i].id == todoItem.id) {
                _todoItems[i] = todoItem
                break
            }
        }
        val nl = _todoItems
        _todoItemsHolder.value = nl
    }

    init {
        for (i in 1..20) {
            val todoItem = TodoItem(
                id = "ID$i",
                text = "Todo ${i.toString().repeat(i * i)}",
                importance = when {
                    i <= 5 -> Importance.LOW
                    i <= 15 -> Importance.MEDIUM
                    else -> Importance.HIGH
                },
                isComplete = i == 4,
                dateCreate = System.currentTimeMillis(),
                dateComplete = if (i==4) System.currentTimeMillis() else null,
                dateEdit = if (i in 2..7) System.currentTimeMillis() else null
            )
            _todoItems.add(todoItem)
        }
        val nl = _todoItems
        _todoItemsHolder.value = nl
    }
}