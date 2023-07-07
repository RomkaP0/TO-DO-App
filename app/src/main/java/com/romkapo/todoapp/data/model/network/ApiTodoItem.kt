package com.romkapo.todoapp.data.model.network

import com.google.gson.annotations.SerializedName
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.utils.parseImportanceToLocal
import com.romkapo.todoapp.utils.parseToNetwork


data class ApiTodoItem(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("deadline") val deadline: Long,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val changedAt: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String,
)

fun TodoItem.toNetworkItem(deviceID:String): ApiTodoItem {
    return ApiTodoItem(
        id = id,
        text = text,
        importance = importance.parseToNetwork(),
        deadline = if (dateComplete==null) 0L else dateComplete!!,
        done = isComplete,
        color = "",
        createdAt = dateCreate,
        changedAt = if (dateEdit==null) 0L else dateEdit!!,
        lastUpdatedBy = deviceID,
    )
}

fun ApiTodoItem.toTodoItem(): TodoItem {
    return TodoItem(
        id = id,
        text = text,
        importance = parseImportanceToLocal(importance),
        dateComplete = if (deadline ==0L) null else deadline,
        isComplete = done,
        dateCreate = createdAt,
        dateEdit = if (changedAt ==0L) null else changedAt,
    )
}
