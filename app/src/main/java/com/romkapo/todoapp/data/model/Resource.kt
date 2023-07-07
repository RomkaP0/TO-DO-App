package com.romkapo.todoapp.data.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T, val message:String): Resource<T>()
    data class Loading<out T>(val message:String): Resource<T>()
    data class Error<T>(val message:String): Resource<T>()
}