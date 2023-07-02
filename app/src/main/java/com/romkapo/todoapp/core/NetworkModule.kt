package com.romkapo.todoapp.core

import android.content.Context
import com.romkapo.todoapp.data.network.AuthInterceptor
import com.romkapo.todoapp.data.network.ConnectionManagerObserver
import com.romkapo.todoapp.data.network.TodoAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = (HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun toDoApiService(
        okHttpClient: OkHttpClient,
    ): TodoAPI {
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TodoAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectionManager(@ApplicationContext context: Context): ConnectionManagerObserver {
        return ConnectionManagerObserver(context = context)
    }


}