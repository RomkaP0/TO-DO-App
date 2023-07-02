package com.romkapo.todoapp.data.model.network

import com.google.gson.annotations.SerializedName

data class TodoItemListRequest(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("list") val list: List<ApiTodoItem>
)
