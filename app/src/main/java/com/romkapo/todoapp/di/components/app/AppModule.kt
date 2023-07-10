package com.romkapo.todoapp.di.components.app

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.data.repository.MainRepositoryImpl
import com.romkapo.todoapp.di.components.auth.AuthFragmentComponent
import com.romkapo.todoapp.di.components.edit.AddEditFragmentComponent
import com.romkapo.todoapp.di.components.list.TodoListItemFragmentComponent
import com.romkapo.todoapp.di.components.main.MainActivityComponent
import com.romkapo.todoapp.di.modules.DatabaseModule
import com.romkapo.todoapp.di.modules.NetworkModule
import com.romkapo.todoapp.di.modules.SharedPrefModule
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
