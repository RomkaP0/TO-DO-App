package com.romkapo.todoapp.utils

object Constants {
    const val LAST_REVISION = "X-Last-Known-Revision"

    // Retry
    const val UPDATE_LOCAL_WORKER_NAME = "UpdateLocalDataWorker"
    const val UPDATE_PERIOD = 8L

    // AppSharedPreferences
    const val SHARED_PREF_NAME = "shared_preferences"
    const val TOKEN_KEY = "token_key"
    const val REVISION_KEY = "revision_key"

    //Exceptions
    const val CLIENT_EXCEPTION = 400
    const val SYNC_EXCEPTION = 401
    const val NOT_FOUND_EXCEPTION = 404
    const val NET_EXCEPTION_DOWN = 500
    const val NET_EXCEPTION_UP = 500

    const val OK ="ok"


}
