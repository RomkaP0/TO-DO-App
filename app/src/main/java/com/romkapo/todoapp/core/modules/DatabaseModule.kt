package com.romkapo.todoapp.core.modules

import android.content.Context
import androidx.room.Room
import com.romkapo.todoapp.data.room.AppDatabase
import com.romkapo.todoapp.data.room.TodoDAO
import com.romkapo.todoapp.data.room.TodoOperationDAO
import dagger.Module
import dagger.Provides

@Module
interface DatabaseModule {

    companion object {

        @Provides
        fun provideDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "TodoDatabase.db",
            ).build()
        }

        @Provides
        fun provideItemDao(
            database: AppDatabase,
        ): TodoDAO = database.getTodoDAO()

        @Provides
        fun provideOperationDao(
            database: AppDatabase,
        ): TodoOperationDAO = database.getOperationsDAO()
    }
}
