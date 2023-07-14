package com.romkapo.todoapp.di.components.list

import com.romkapo.todoapp.di.components.ViewModelFactoryModule
import com.romkapo.todoapp.di.components.common.ViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [TodoListItemModule::class, ViewModelFactoryModule::class])
@TodoListItemFragmentScope
interface TodoListItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListItemFragmentComponent
    }
    fun getViewModelFactory(): ViewModelFactory

}
