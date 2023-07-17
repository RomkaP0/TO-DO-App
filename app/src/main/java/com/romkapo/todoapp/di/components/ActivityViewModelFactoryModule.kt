package com.romkapo.todoapp.di.components

import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.di.components.common.ActivityViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ActivityViewModelFactoryModule {
    @Binds
    fun viewModelFactory(factory: ActivityViewModelFactory): ViewModelProvider.Factory
}