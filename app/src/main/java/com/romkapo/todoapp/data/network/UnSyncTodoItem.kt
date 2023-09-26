package com.romkapo.todoapp.data.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romkapo.todoapp.data.local.TodoItem

@Entity(tableName = "UnSyncOperations")
data class UnSyncTodoItem(
    var id: String,
    var timestamp: Long,
    var type: String,
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0,
)
fun TodoItem.toOperationItem(type: String): UnSyncTodoItem {
    return UnSyncTodoItem(id, System.currentTimeMillis(), type)
}
