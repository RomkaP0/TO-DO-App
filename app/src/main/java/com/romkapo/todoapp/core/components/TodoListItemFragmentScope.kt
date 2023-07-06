package com.romkapo.todoapp.core.components

import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class TodoListItemFragmentScope

@Subcomponent
@TodoListItemFragmentScope
interface TodoListItemFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListItemFragmentComponent
    }
}