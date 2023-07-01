package com.romkapo.todoapp.data.model.network

import com.google.gson.annotations.SerializedName

data class TodoItemListRequest(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<ApiTodoItem>
)
