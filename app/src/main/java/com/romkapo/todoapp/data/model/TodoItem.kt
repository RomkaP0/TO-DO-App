package com.romkapo.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romkapo.todoapp.utils.Importance

@Entity(tableName = "todoItems")
data class TodoItem(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var text: String,
    var importance: Importance,
    var dateCreate: Long,
    var dateComplete: Long? = null,
    var dateEdit: Long? = null,
    var isComplete: Boolean = false,
) {
    constructor() : this("-1", "", Importance.MEDIUM, 0L, null, null, false)
}
