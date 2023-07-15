package com.romkapo.todoapp.di.components.list

import com.romkapo.todoapp.di.components.ComposeViewModelFactoryModule
import com.romkapo.todoapp.di.components.common.ComposeViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [TodoListItemModule::class, ComposeViewModelFactoryModule::class])
@TodoListItemFragmentScope
interface TodoListItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListItemFragmentComponent
    }
    fun getViewModelFactory(): ComposeViewModelFactory

}
