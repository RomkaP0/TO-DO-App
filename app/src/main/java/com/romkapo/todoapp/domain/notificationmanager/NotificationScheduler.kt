package com.romkapo.todoapp.domain.notificationmanager

import com.romkapo.todoapp.data.local.TodoItem

interface NotificationScheduler {
    fun schedule(item: TodoItem)
    fun cancel(item: TodoItem)
    fun cancelAll()
}
