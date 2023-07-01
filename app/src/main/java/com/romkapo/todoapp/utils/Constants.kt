package com.romkapo.todoapp.utils

object Constants {
    const val LAST_REVISION = "X-Last-Known-Revision"

    //Retry
    const val UPDATE_LOCAL_WORKER_NAME = "UpdateLocalDataWorker"
    const val MAX_RETRY_COUNT = 3
    const val RETRY_OFFSET = 3000L
    const val UPDATE_PERIOD = 8L

    //AppSharedPreferences
    const val SHARED_PREF_NAME = "shared_preferences"
    const val TOKEN_KEY = "token_key"
    const val REVISION_KEY = "revision_key"

}