package com.romkapo.todoapp.di.components.auth

import com.romkapo.todoapp.di.components.ComposeViewModelFactoryModule
import com.romkapo.todoapp.di.components.common.ComposeViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [AuthFragmentModule::class, ComposeViewModelFactoryModule::class])
@AuthFragmentScope
interface AuthFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthFragmentComponent
    }
    fun getViewModelFactory(): ComposeViewModelFactory
}
