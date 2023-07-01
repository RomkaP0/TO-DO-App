package com.romkapo.todoapp.data.network

sealed class Resource<out T> {
    data class Success<out T>(val data: T): Resource<T>()
    data class Loading<out T>(val isLoading: Boolean): Resource<T>()
    data class Error<T>(val nameError: String): Resource<T>()
}