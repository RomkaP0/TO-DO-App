package com.romkapo.todoapp.presentation.screen.todolistitems

import com.romkapo.todoapp.data.model.TodoItem

data class TodoScreenStates(
    val todoList: List<TodoItem>,
    val todoFullList: List<TodoItem>,
    val isCheckedShown: Boolean,
    val countOfCompleted: Int,
)
