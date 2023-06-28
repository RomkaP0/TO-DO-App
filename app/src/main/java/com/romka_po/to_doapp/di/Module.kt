package com.romka_po.to_doapp.di

import android.content.Context
import com.romka_po.to_doapp.repository.TodoItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideRepository(@ApplicationContext context:Context): TodoItemsRepository{
        return TodoItemsRepository()
    }
}