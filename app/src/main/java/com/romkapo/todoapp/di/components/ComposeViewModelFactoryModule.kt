package com.romkapo.todoapp.di.components

import com.romkapo.todoapp.di.components.common.ComposeViewModelFactory
import com.romkapo.todoapp.di.components.common.DaggerViewModelAssistedFactory
import dagger.Binds
import dagger.Module

@Module
interface ComposeViewModelFactoryModule {
    @Binds
    fun bindsDaggerViewModelAssistedFactory(factory: DaggerViewModelAssistedFactory): ComposeViewModelFactory
}
