package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.domain.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoItemListViewModel @Inject constructor(
    private val repository: MainRepository,
    private val appSharedPreferences: AppSharedPreferences,
) :
    ViewModel() {
    private var getDataJob: Job? = null
    private var updateDataJob: Job? = null

    private val _listTodoItem = MutableSharedFlow<List<TodoItem>>()
    val listTodoItem: SharedFlow<List<TodoItem>> get() = _listTodoItem.asSharedFlow()

    val countOfComplete: Flow<Int> = _listTodoItem.map { it -> it.count { it.isComplete } }
    var showUnchecked = true

    fun getAllList() {
        getDataJob?.cancel()
        getDataJob = viewModelScope.launch(Dispatchers.IO) {
            _listTodoItem.emitAll(repository.getTodoList())
        }
    }

    fun changeShow() {
        showUnchecked = !showUnchecked
        getAllList()
    }

    fun addTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTodoItem(todoItem)
    }

    fun removeTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoItem(todoItem)
    }

    fun editStateTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodoItem(todoItem)
    }

    fun refresh() {
        updateDataJob?.cancel()
        updateDataJob = viewModelScope.launch(Dispatchers.IO) {
            repository.fetchTasks()
        }
    }

    fun logOut() {
        appSharedPreferences.setCurrentToken("")
    }

    override fun onCleared() {
        getDataJob?.cancel()
        super.onCleared()
    }
}
