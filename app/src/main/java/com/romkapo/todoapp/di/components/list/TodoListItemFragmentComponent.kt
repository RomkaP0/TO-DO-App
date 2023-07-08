package com.romkapo.todoapp.di.components.list

import com.romkapo.todoapp.presentation.screen.todolistitems.TodoListFragment
import dagger.Subcomponent

@Subcomponent(modules = [TodoListItemModule::class])
@TodoListItemFragmentScope
interface TodoListItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListItemFragmentComponent
    }
    fun inject(todoItemListFragment: TodoListFragment)
}
