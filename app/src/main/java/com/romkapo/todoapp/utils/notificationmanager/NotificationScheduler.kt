package com.romkapo.todoapp.utils.notificationmanager

import com.romkapo.todoapp.data.model.TodoItem

interface NotificationScheduler {
    fun schedule(item: TodoItem)
    fun cancel(item: TodoItem)
    fun cancelAll()
}