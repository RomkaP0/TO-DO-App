package com.romkapo.todoapp.core.components.app

import com.romkapo.todoapp.core.components.auth.AuthFragmentComponent
import com.romkapo.todoapp.core.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.core.components.list.TodoListItemFragmentComponent
import com.romkapo.todoapp.core.components.main.MainActivityComponent
import com.romkapo.todoapp.core.modules.LocalModule
import com.romkapo.todoapp.core.modules.NetworkModule
import com.romkapo.todoapp.data.repository.MainRepositoryImpl
import com.romkapo.todoapp.domain.MainRepository
import dagger.Binds
import dagger.Module

@Module(
    includes = [LocalModule::class, NetworkModule::class],
    subcomponents = [AuthFragmentComponent::class,
        TodoListItemFragmentComponent::class,
        AddEditFragmentComponent::class,
        MainActivityComponent::class]
)
interface AppModule {
    @Binds
    @AppScope
    fun provideRepository(impl: MainRepositoryImpl): MainRepository
}