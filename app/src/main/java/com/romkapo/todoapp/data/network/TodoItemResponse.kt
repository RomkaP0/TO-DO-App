package com.romkapo.todoapp.data.network

import com.google.gson.annotations.SerializedName

data class TodoItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: ApiTodoItem,
    @SerializedName("revision") val revision: Int,
)
