package com.romkapo.todoapp.core.components.edit

import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.core.components.app.ViewModelKeys
import com.romkapo.todoapp.presentation.screen.addedititem.AddEditItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AddEditFragmentModule{
    @Binds
    @IntoMap
    @ViewModelKeys(AddEditItemViewModel::class)
    fun provideAddEditViewModel(addEditItemViewModel: AddEditItemViewModel): ViewModel
}
