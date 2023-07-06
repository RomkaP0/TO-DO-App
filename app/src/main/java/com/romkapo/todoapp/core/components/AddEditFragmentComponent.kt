package com.romkapo.todoapp.core.components

import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class AddEditFragmentScope

@Subcomponent
@AddEditFragmentScope
interface AddEditFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditFragmentComponent
    }
}