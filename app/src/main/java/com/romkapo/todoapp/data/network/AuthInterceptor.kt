package com.romkapo.todoapp.data.network

import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = appSharedPreferences.getCurrentToken()
        return chain.proceed(
            chain.request().newBuilder().addHeader(
                "Authorization",
                "OAuth $token",
            ).build(),
        )
    }
}
