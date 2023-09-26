package com.romkapo.todoapp.ui.screen.addedititem

data class AddEditScreenStates(
    val id: String,
    val text: String,
    val importance: String,
    val hasDeadline: Boolean,
    val dateCreate: Long,
    val deadline: Long,
    val isNew: Boolean,
    val isBottomSheetOpened: Boolean,
    val isHighlight: Boolean,
    val isDialogShown: Boolean,
)
