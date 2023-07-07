package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.core.components.app.AppScope
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.domain.MainRepository
import javax.inject.Inject

@AppScope
class TodoListViewModelFactory @Inject constructor(
    private val repository: MainRepository,
    private val appSharedPreferences: AppSharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoItemListViewModel(repository, appSharedPreferences) as T
    }
}