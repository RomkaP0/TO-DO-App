package com.romkapo.todoapp.core.components

import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class ActivityScope

@Subcomponent
@ActivityScope
interface ActivityComponent {
    fun addEditFragmentComponentFactory(): AddEditFragmentComponent.Factory
    fun todoListItemFragmentComponentFactory(): TodoListItemFragmentComponent.Factory
    fun authFragmentComponentFactory(): AuthFragmentComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}