package com.romkapo.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.network.ConnectionManagerObserver
import com.romkapo.todoapp.data.network.map
import com.romkapo.todoapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkStatusTracker: ConnectionManagerObserver,
    private val repository: MainRepository
) : ViewModel() {

    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )

    fun updateRepository()  = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask()
    }
}


sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}