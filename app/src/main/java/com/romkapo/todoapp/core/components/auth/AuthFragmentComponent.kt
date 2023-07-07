package com.romkapo.todoapp.core.components.auth

import com.romkapo.todoapp.presentation.screen.auth.AuthFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthFragmentModule::class])
interface AuthFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthFragmentComponent
    }
    fun inject(authFragment: AuthFragment)
}
