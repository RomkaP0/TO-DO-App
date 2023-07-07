package com.romkapo.todoapp.data.model

sealed class Resource {
    data class Success(val message: String) : Resource()
    data class Loading(val message: String) : Resource()
    data class Error(val message: String) : Resource()
}
