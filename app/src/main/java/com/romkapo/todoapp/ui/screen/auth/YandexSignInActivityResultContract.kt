package com.romkapo.todoapp.ui.screen.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk

class YandexSignInActivityResultContract :
    ActivityResultContract<YandexAuthSdk, Pair<Int, Intent?>>() {

    override fun createIntent(context: Context, input: YandexAuthSdk): Intent {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        return input.createLoginIntent(loginOptionsBuilder.build())
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Int, Intent?> {
        return Pair(resultCode, intent)
    }
}
