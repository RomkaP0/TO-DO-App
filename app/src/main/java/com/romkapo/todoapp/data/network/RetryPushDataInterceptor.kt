package com.romkapo.todoapp.data.network

import com.romkapo.todoapp.utils.Constants.MAX_RETRY_COUNT
import com.romkapo.todoapp.utils.Constants.RETRY_OFFSET
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class RetryInterceptor @Inject constructor() : Interceptor {

    private var tryCount = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return if (response.code == 500) {

            if (tryCount < MAX_RETRY_COUNT) {
                Thread.sleep(RETRY_OFFSET)
                tryCount++
                response.close()
                chain.call().clone().execute()
            }

            response.newBuilder()
                .code(401) // Whatever code
                .body("".toResponseBody(null)) // Whatever body
                .protocol(Protocol.HTTP_2)
                .message("Network Error")
                .request(chain.request())
                .build()
        } else {
            response
        }
    }
}