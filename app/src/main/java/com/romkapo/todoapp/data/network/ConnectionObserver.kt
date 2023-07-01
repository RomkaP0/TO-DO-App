package com.romkapo.todoapp.data.network

import kotlinx.coroutines.flow.Flow

interface ConnectionObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable
    }
}