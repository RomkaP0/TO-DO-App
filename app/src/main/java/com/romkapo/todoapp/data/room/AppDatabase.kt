package com.romkapo.todoapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.UnSyncTodoItem

@Database(entities = [TodoItem::class, UnSyncTodoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTodoDAO(): TodoDAO
    abstract fun getOperationsDAO(): TodoOperationDAO
}
