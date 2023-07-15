package com.romkapo.todoapp.di.components.auth

import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.presentation.screen.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AuthFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKeys(AuthViewModel::class)
    fun provideAddEditViewModelFactory(factory: AuthViewModel.Factory): ViewModelAssistedFactory<*>
}
