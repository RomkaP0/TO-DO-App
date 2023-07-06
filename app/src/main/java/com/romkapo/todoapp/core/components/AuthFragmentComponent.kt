package com.romkapo.todoapp.core.components

import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class AuthFragmentScope

@Subcomponent
@AuthFragmentScope
interface AuthFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthFragmentComponent
    }
}