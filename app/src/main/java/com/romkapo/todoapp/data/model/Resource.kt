package com.romkapo.todoapp.data.model

sealed class Resource {
    object Success : Resource()
    data class Error(val exception:ApiException) : Resource()
}
