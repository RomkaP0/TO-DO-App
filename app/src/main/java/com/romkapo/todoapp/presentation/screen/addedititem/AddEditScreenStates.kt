package com.romkapo.todoapp.presentation.screen.addedititem

import com.romkapo.todoapp.data.model.TodoItem

data class AddEditScreenStates(
    val oldTodoItem: TodoItem? = null,
    val id:String,
    val text:String,
    val importance:String,
    val hasDeadline:Boolean,
    val dateCreate:Long,
    val deadline:Long,
    val isNew:Boolean,
    val isBottomSheetOpened:Boolean,
    val isHighlight:Boolean,
    val isDialogShown:Boolean
)