package com.romkapo.todoapp.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romkapo.todoapp.data.local.TodoItem
import com.romkapo.todoapp.data.network.UnSyncTodoItem

@Database(entities = [TodoItem::class, UnSyncTodoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTodoDAO(): TodoDAO
    abstract fun getOperationsDAO(): TodoOperationDAO
}
