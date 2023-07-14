package com.romkapo.todoapp.di.components.main

import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.presentation.screen.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKeys(MainViewModel::class)
    fun provideMainViewModelFactory(factory:MainViewModel.Factory): ViewModelAssistedFactory<*>
}
