package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.network.ConnectionManagerObserver
import com.romkapo.todoapp.data.network.ConnectionObserver
import com.romkapo.todoapp.data.network.Resource
import com.romkapo.todoapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemListViewModel @Inject constructor(
    private val repository: MainRepository,
    private val connection: ConnectionManagerObserver,
) :
    ViewModel() {
    private var job: Job? = null

    private val _listTodoItem = MutableSharedFlow<List<TodoItem>>()
    val listTodoItem: SharedFlow<List<TodoItem>> get() = _listTodoItem

    val countOfComplete: Flow<Int> = _listTodoItem.map { list -> list.count { it.isComplete } }
    var showUnchecked = true

    private val _status = MutableStateFlow(ConnectionObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _loadingState =
        MutableStateFlow<Resource<Any>>(Resource.Success("Loaded from rood complete!"))
    val loadingState: StateFlow<Resource<Any>> = _loadingState.asStateFlow()

    init {
        observeNetwork()
        loadData()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun loadData() {
        job?.cancel()
        if (showUnchecked) {
            loadLocalData()
        } else {
            getUncheckedTodoList()
        }
    }

    private fun loadLocalData() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _listTodoItem.emitAll(repository.getAllToDoItems())
        }
    }

    private fun getUncheckedTodoList() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _listTodoItem.emitAll(repository.getUncheckedToDoItems())
        }
    }

    fun changeShow() {
        showUnchecked = !showUnchecked
        loadData()
    }

    fun addTodoItemAt(item: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.createItem(item)
    }

    fun removeItemWithID(id: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteToDoItem(id)
    }

    fun changeTaskDone(task: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStatusToDoItem(task.id, task.isComplete)
        }
    }

    fun loadRemoteList() {
        if (status.value == ConnectionObserver.Status.Available) {
            _loadingState.value = Resource.Loading(true)
            viewModelScope.launch(Dispatchers.IO) {
                _loadingState.emit(repository.getRemoteTasks())
            }
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

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}