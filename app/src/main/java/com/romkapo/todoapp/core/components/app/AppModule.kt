package com.romkapo.todoapp.core.components.app

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.core.components.auth.AuthFragmentComponent
import com.romkapo.todoapp.core.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.core.components.list.TodoListItemFragmentComponent
import com.romkapo.todoapp.core.components.main.MainActivityComponent
import com.romkapo.todoapp.core.modules.DatabaseModule
import com.romkapo.todoapp.core.modules.NetworkModule
import com.romkapo.todoapp.core.modules.SharedPrefModule
import com.romkapo.todoapp.data.repository.MainRepositoryImpl
import com.romkapo.todoapp.domain.MainRepository
import com.romkapo.todoapp.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module(
    includes = [DatabaseModule::class, NetworkModule::class, SharedPrefModule::class],
    subcomponents = [
        AuthFragmentComponent::class,
        TodoListItemFragmentComponent::class,
        AddEditFragmentComponent::class,
        MainActivityComponent::class,
    ],
)
interface AppModule {

    @Binds
    @AppScope
    fun provideRepository(impl: MainRepositoryImpl): MainRepository

    @Binds
    fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    companion object {
        @Provides
        @AppScope
        fun provideScope(context: Context) = CoroutineScope(SupervisorJob())
    }
}
