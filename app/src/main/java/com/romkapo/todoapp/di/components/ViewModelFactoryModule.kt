package com.romkapo.todoapp.di.components

import com.romkapo.todoapp.di.components.common.DaggerViewModelAssistedFactory
import com.romkapo.todoapp.di.components.common.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {
    @Binds
    fun bindsDaggerViewModelAssistedFactory(factory: DaggerViewModelAssistedFactory): ViewModelFactory
}