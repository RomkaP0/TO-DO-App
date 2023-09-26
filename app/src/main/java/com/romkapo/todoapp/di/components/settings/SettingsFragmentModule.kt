package com.romkapo.todoapp.di.components.settings

import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.ui.screen.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SettingsFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKeys(SettingsViewModel::class)
    fun provideSettingsViewModelFactory(factory: SettingsViewModel.Factory): ViewModelAssistedFactory<*>
}
