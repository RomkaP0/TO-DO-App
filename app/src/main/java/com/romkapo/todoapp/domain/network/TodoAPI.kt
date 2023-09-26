package com.romkapo.todoapp.domain.network

import com.romkapo.todoapp.data.network.TodoItemListRequest
import com.romkapo.todoapp.data.network.TodoItemListResponse
import com.romkapo.todoapp.data.network.TodoItemRequest
import com.romkapo.todoapp.data.network.TodoItemResponse
import com.romkapo.todoapp.utils.Constants.LAST_REVISION
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoAPI {
    @GET("list")
    suspend fun getList(): Response<TodoItemListResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header(LAST_REVISION) revision: Int,
        @Body body: TodoItemListRequest,
    ): Response<TodoItemListResponse>

    @POST("list")
    suspend fun addItem(
        @Header(LAST_REVISION) revision: Int,
        @Body newItem: TodoItemRequest,
    ): Response<TodoItemResponse>

    @PUT("list/{id}")
    suspend fun updateItem(
        @Header(LAST_REVISION) revision: Int,
        @Path("id") itemId: String,
        @Body body: TodoItemRequest,
    ): Response<TodoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header(LAST_REVISION) revision: Int,
        @Path("id") itemId: String,
    ): Response<TodoItemResponse>
}
