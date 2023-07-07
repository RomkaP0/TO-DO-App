package com.romkapo.todoapp.core.components.main

import com.romkapo.todoapp.presentation.screen.main.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }
    fun inject(mainActivity: MainActivity)

}