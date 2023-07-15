package com.romkapo.todoapp.di.components.app

import android.content.Context
import com.romkapo.todoapp.App
import com.romkapo.todoapp.di.components.auth.AuthFragmentComponent
import com.romkapo.todoapp.di.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.di.components.list.TodoListItemFragmentComponent
import com.romkapo.todoapp.di.components.main.MainActivityComponent
import com.romkapo.todoapp.di.components.settings.SettingsFragmentComponent
import com.romkapo.todoapp.utils.notificationmanager.NotificationPostponeReceiver
import com.romkapo.todoapp.utils.notificationmanager.NotificationReceiver
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier

@Qualifier
annotation class AppContext

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun todoItemListFragmentComponentFactory(): TodoListItemFragmentComponent.Factory

    fun addEditFragmentComponentFactory(): AddEditFragmentComponent.Factory
    fun authFragmentComponentFactory(): AuthFragmentComponent.Factory
    fun mainActivityComponentFactory(): MainActivityComponent.Factory
    fun settingsFragmentComponentFactory(): SettingsFragmentComponent.Factory

    fun injectNotificationReceiver(receiver: NotificationReceiver)
    fun injectNotificationPostponeReceiver(receiver: NotificationPostponeReceiver)

    fun inject(application: App)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}
