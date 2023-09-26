package com.romkapo.todoapp.ui.screen.todolistitems

import com.romkapo.todoapp.data.local.TodoItem

data class TodoScreenStates(
    val todoList: List<TodoItem>,
    val todoFullList: List<TodoItem>,
    val isCheckedShown: Boolean,
    val countOfCompleted: Int,
)
