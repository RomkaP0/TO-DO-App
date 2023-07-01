package com.romkapo.todoapp.presentation.screen.todoaddedititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.network.ConnectionObserver
import com.romkapo.todoapp.data.network.Resource
import com.romkapo.todoapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditItemViewModel @Inject constructor(private val repository: MainRepository):ViewModel() {
    var completeTimeStamp = System.currentTimeMillis()
    var id = ""

    private val _status = MutableStateFlow(ConnectionObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _loadingState =
        MutableStateFlow<Resource<Any>>(Resource.Success("Loaded from rood complete!"))
    val loadingState: StateFlow<Resource<Any>> = _loadingState.asStateFlow()

    private var _currentItemFlow = MutableStateFlow(TodoItem())
    var currentItemFlow = _currentItemFlow.asStateFlow()
    var currentItem = TodoItem()

    fun addTodoItemAt(item: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.createItem(item)
    }

    fun removeItemWithID(id: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteToDoItem(id)
    }
    fun updateTask(newItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateToDoItem(newItem)
        }
    }
    fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getToDoItemById(id)?.let { _currentItemFlow.emit(it) }
        }
    }

    fun createRemoteTask(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createRemoteTask(todoItem)
        }
    }

    fun deleteRemoteTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRemoteTask(id)
        }
    }

    fun updateRemoteTask(todoItem: TodoItem) {
        val item = todoItem.copy(isComplete = !todoItem.isComplete)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRemoteTask(item)
        }
    }
}