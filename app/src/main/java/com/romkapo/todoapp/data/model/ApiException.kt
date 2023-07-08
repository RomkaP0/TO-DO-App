package com.romkapo.todoapp.data.model

/* Sealed набор различных коллбэков сервера*/
sealed class ApiException : Exception()

object SyncFailedException : ApiException()
object ItemNotFoundException : ApiException()
object BadRequestException : ApiException()
object ClientSideException : ApiException()
object NetworkException : ApiException()
object UpdateFailedException : ApiException()
