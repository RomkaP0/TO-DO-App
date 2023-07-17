package com.romkapo.todoapp.di.components.edit

import com.romkapo.todoapp.di.components.ComposeViewModelFactoryModule
import com.romkapo.todoapp.di.components.common.ComposeViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [AddEditFragmentModule::class, ComposeViewModelFactoryModule::class])
@AddEditFragmentScope
interface AddEditFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditFragmentComponent
    }

    fun getViewModelFactory(): ComposeViewModelFactory

}
