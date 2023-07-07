package com.romkapo.todoapp.core.components.app

import android.content.Context
import com.romkapo.todoapp.App
import com.romkapo.todoapp.core.components.auth.AuthFragmentComponent
import com.romkapo.todoapp.core.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.core.components.list.TodoListItemFragmentComponent
import com.romkapo.todoapp.core.components.main.MainActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier

@Qualifier
annotation class AppContext

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun addEditFragmentComponentFactory(): AddEditFragmentComponent.Factory
    fun authFragmentComponentFactory(): AuthFragmentComponent.Factory
    fun todoItemListFragmentComponentFactory(): TodoListItemFragmentComponent.Factory
    fun mainActivityComponentFactory():MainActivityComponent.Factory
    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}