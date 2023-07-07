package com.romkapo.todoapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.core.components.app.AppScope
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.network.ConnectionManagerObserver
import com.romkapo.todoapp.domain.MainRepository
import com.romkapo.todoapp.presentation.screen.addedititem.AddEditItemViewModel
import com.romkapo.todoapp.presentation.screen.auth.AuthViewModel
import com.romkapo.todoapp.presentation.screen.main.MainViewModel
import com.romkapo.todoapp.presentation.screen.todolistitems.TodoItemListViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(
    private val networkStatusTracker: ConnectionManagerObserver,
    private val repository: MainRepository,
    private val appSharedPreferences: AppSharedPreferences,
    private val coroutineScope: CoroutineScope,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            TodoItemListViewModel::class.java -> TodoItemListViewModel(
                repository,
                appSharedPreferences,
            ) as T

            AddEditItemViewModel::class.java -> AddEditItemViewModel(repository, coroutineScope) as T
            AuthViewModel::class.java -> AuthViewModel(appSharedPreferences) as T
            MainViewModel::class.java -> MainViewModel(networkStatusTracker, repository) as T
            else -> throw IllegalStateException("Unknown view model class")
        }
    }
}
