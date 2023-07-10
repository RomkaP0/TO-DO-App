package com.romkapo.todoapp.data.model.network

import com.google.gson.annotations.SerializedName

data class TodoItemRequest(
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: ApiTodoItem,
)
