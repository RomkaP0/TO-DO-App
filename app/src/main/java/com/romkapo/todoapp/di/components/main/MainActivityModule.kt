package com.romkapo.todoapp.di.components.main

import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.ui.screen.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKeys(MainViewModel::class)
    fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel
}
