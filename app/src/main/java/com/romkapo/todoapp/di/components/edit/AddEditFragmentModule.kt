package com.romkapo.todoapp.di.components.edit

import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.ui.screen.addedititem.AddEditItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AddEditFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKeys(AddEditItemViewModel::class)
    fun provideAddEditViewModelFactory(factory: AddEditItemViewModel.Factory): ViewModelAssistedFactory<*>
}
