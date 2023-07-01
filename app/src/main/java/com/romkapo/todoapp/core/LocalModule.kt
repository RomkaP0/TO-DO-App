package com.romkapo.todoapp.core

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.network.TodoAPI
import com.romkapo.todoapp.data.repository.MainRepository
import com.romkapo.todoapp.data.room.AppDatabase
import com.romkapo.todoapp.data.room.TodoDAO
import com.romkapo.todoapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "TodoDatabase.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDBDao(
        database: AppDatabase
    ): TodoDAO = database.getTodoDAO()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE
        )
    @Provides
    @Singleton
    fun provideAppSharedPreferences(preferences: SharedPreferences): AppSharedPreferences {
        return AppSharedPreferences(preferences)
    }

    @Singleton
    @Provides
    fun provideRepository(todoDAO: TodoDAO, todoAPI: TodoAPI, sharedPreferences: AppSharedPreferences): MainRepository {
        return MainRepository(todoDAO,todoAPI,sharedPreferences)
    }
}