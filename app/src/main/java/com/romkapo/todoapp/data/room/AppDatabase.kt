package com.romkapo.todoapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romkapo.todoapp.data.model.TodoItem

@Database(entities = [TodoItem::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun getTodoDAO(): TodoDAO
}