package com.romkapo.todoapp.core.components.list

import com.romkapo.todoapp.presentation.screen.todolistitems.TodoListFragment
import dagger.Subcomponent

@Subcomponent(modules = [TodoListItemModule::class])
interface TodoListItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListItemFragmentComponent
    }
    fun inject(todoItemListFragment: TodoListFragment)
}
