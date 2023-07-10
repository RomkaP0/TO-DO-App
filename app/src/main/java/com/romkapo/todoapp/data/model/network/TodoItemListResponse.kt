package com.romkapo.todoapp.data.model.network

import com.google.gson.annotations.SerializedName

data class TodoItemListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<ApiTodoItem>,
    @SerializedName("revision") val revision: Int,
)
