package com.romkapo.todoapp.data.network

import com.romkapo.todoapp.data.model.network.TodoItemListRequest
import com.romkapo.todoapp.data.model.network.TodoItemListResponse
import com.romkapo.todoapp.data.model.network.TodoItemRequest
import com.romkapo.todoapp.data.model.network.TodoItemResponse
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

    @GET("list/{id}")
    suspend fun getItemById(@Path("id") itemId: String): Response<TodoItemResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body body: TodoItemListRequest
    ): Response<TodoItemListResponse>

    @POST("list")
    suspend fun addItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body newItem: TodoItemRequest
    ): Response<TodoItemResponse>

    @PUT("list/{id}")
    suspend fun updateItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") itemId: String,
        @Body body: TodoItemRequest
    ): Response<TodoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") itemId: String,
    ): Response<TodoItemResponse>
}