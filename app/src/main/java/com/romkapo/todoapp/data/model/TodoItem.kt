package com.romkapo.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
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
){
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
