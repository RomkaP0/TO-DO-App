package com.romkapo.todoapp.di.components.main

import com.romkapo.todoapp.di.components.ActivityViewModelFactoryModule
import com.romkapo.todoapp.ui.screen.main.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModule::class, ActivityViewModelFactoryModule::class])
@MainActivityScope
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }
    fun inject(mainActivity: MainActivity)
}
