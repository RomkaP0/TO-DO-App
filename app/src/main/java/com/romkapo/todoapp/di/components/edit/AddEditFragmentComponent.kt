package com.romkapo.todoapp.di.components.edit

import com.romkapo.todoapp.di.components.ViewModelFactoryModule
import com.romkapo.todoapp.di.components.common.ViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [AddEditFragmentModule::class, ViewModelFactoryModule::class])
@AddEditFragmentScope
interface AddEditFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditFragmentComponent
    }

    fun getViewModelFactory(): ViewModelFactory

}
