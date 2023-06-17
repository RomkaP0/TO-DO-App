package com.romka_po.to_doapp.repository

import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.utils.Importance
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoItemsRepository @Inject constructor() {
    private var _todoItems = mutableListOf<TodoItem>()
    private val todoItemsHolder = MutableSharedFlow<List<TodoItem>>()
    val todoItems: SharedFlow<List<TodoItem>> get() = todoItemsHolder

    init {
        MainScope().launch {
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
                    dateComplete = if (i == 4) System.currentTimeMillis() else null,
                    dateEdit = if (i in 2..7) System.currentTimeMillis() else null
                )
                _todoItems.add(todoItem)
            }
            emitData()
        }
    }

    suspend fun addData(item: TodoItem, position: Int = _todoItems.size) {
        _todoItems.add(position, item)
        emitData()
    }

    suspend fun removeTodoItemWithID(id: String) {
        for (i in 0 until _todoItems.size) {
            if (_todoItems[i].id == id) {
                _todoItems.removeAt(i)
                break
            }
        }
        emitData()
    }

    suspend fun editTodoItem(todoItem: TodoItem) {
        for (i in 0 until _todoItems.size) {
            if (_todoItems[i].id == todoItem.id) {
                _todoItems[i] = todoItem
                break
            }
        }
        emitData()
    }

    suspend fun forceEmitData(){
        emitData()
    }

    private suspend fun emitData() {
        val nl = _todoItems
        todoItemsHolder.emit(nl)
    }
}