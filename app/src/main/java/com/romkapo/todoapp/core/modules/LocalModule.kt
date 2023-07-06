package com.romkapo.todoapp.core.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.room.AppDatabase
import com.romkapo.todoapp.data.room.TodoDAO
import com.romkapo.todoapp.utils.Constants
import dagger.Module
import dagger.Provides

@Module
interface LocalModule {

    companion object {

        @Provides
        fun provideDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "TodoDatabase.db"
            ).build()
        }

        @Provides
        fun provideDBDao(
            database: AppDatabase
        ): TodoDAO = database.getTodoDAO()

        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(
                Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE
            )

        @Provides
        fun provideAppSharedPreferences(preferences: SharedPreferences): AppSharedPreferences {
            return AppSharedPreferences(preferences)
        }
    }
}