package com.romkapo.todoapp.data.network

import com.google.gson.annotations.SerializedName

data class TodoItemListRequest(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<ApiTodoItem>,
)
