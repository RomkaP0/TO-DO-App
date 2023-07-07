package com.romkapo.todoapp.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.core.components.app.AppScope
import com.romkapo.todoapp.data.network.ConnectionManagerObserver
import com.romkapo.todoapp.domain.MainRepository
import javax.inject.Inject

@AppScope
class MainViewModelFactory @Inject constructor(
    private val networkStatusTracker: ConnectionManagerObserver,
    private val repository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(networkStatusTracker, repository) as T
    }
}