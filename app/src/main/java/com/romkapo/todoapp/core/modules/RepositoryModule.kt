package com.romkapo.todoapp.core.modules

import com.romkapo.todoapp.core.components.AppScope
import com.romkapo.todoapp.data.repository.MainRepositoryImpl
import com.romkapo.todoapp.domain.MainRepository
import dagger.Binds
import dagger.Module

@Module(includes = [LocalModule::class, NetworkModule::class])
interface RepositoryModule {
    @Binds
    @AppScope
    fun provideRepository(impl: MainRepositoryImpl): MainRepository

}