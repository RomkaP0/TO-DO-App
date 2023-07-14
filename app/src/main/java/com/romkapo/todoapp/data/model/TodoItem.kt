package com.romkapo.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romkapo.todoapp.utils.Importance

@Entity(tableName = "todoItems")
data class TodoItem(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val text: String,
    val importance: Importance,
    val dateCreate: Long,
    val dateComplete: Long? = null,
    val dateEdit: Long? = null,
    val isComplete: Boolean = false,
)