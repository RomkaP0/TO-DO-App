package com.romkapo.todoapp.data.model

sealed class ApiException : Exception()

object SyncFailedException : ApiException()
object ItemNotFoundException : ApiException()
object BadRequestException : ApiException()
object ClientSideException : ApiException()
object NetworkException : ApiException()
object UpdateFailedException : ApiException()