package com.romkapo.todoapp.data.model

/* Enum действий для таблицы несинхронизированных данных*/
enum class UnSyncAction(val label: String) {
    ADD("add"),
    EDIT("edit"),
    DELETE("delete"),
}
