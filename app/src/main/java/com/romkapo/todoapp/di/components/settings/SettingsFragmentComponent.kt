package com.romkapo.todoapp.di.components.settings

import com.romkapo.todoapp.di.components.ComposeViewModelFactoryModule
import com.romkapo.todoapp.di.components.common.ComposeViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [SettingsFragmentModule::class, ComposeViewModelFactoryModule::class])
@SettingsFragmentScope
interface SettingsFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsFragmentComponent
    }

    fun getViewModelFactory(): ComposeViewModelFactory
}
