package com.romkapo.todoapp.data.local

sealed class Resource {
    object Success : Resource()
    data class Error(val exception: ApiException) : Resource()
}
