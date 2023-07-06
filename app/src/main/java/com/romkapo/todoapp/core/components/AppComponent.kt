package com.romkapo.todoapp.core.components

import android.content.Context
import com.romkapo.todoapp.App
import com.romkapo.todoapp.core.modules.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class AppScope

@Qualifier
annotation class AppContext

@AppScope
@Component(modules = [RepositoryModule::class])
interface AppComponent {

    fun activityComponentFactory(): ActivityComponent.Factory

    fun inject(application: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @AppContext
            appContext: Context
        ): AppComponent
    }
}