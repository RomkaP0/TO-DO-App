package com.romkapo.todoapp.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.data.network.ConnectionManagerObserver
import com.romkapo.todoapp.data.network.map
import com.romkapo.todoapp.domain.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel (
    networkStatusTracker: ConnectionManagerObserver,
    private val repository: MainRepository
) : ViewModel() {

    init {
        updateRepository()
    }


    private val _stateRequest = MutableStateFlow<Resource>(Resource.Success(""))
    val stateRequest get() = _stateRequest.asStateFlow()

    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )

    fun listenStateRequest() = viewModelScope.launch(Dispatchers.IO){
         repository.stateRequest.collectLatest {
        _stateRequest.value = it
        }
    }

    fun updateRepository()  = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask()
    }
}


sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}