package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemListViewModel @Inject constructor(
    private val repository: MainRepository,
    private val appSharedPreferences: AppSharedPreferences
) :
    ViewModel() {
    private var job: Job? = null

    private val _listTodoItem = MutableSharedFlow<List<TodoItem>>()
    val listTodoItem: SharedFlow<List<TodoItem>> get() = _listTodoItem.asSharedFlow()

    val countOfComplete: Flow<Int> = _listTodoItem.map { it -> it.count { it.isComplete } }
    var showUnchecked = true
    val _result = MutableSharedFlow<Boolean>()
    val result:SharedFlow<Boolean> get() = _result.asSharedFlow()

    init {
        getAllList()
    }

    fun getAllList() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _listTodoItem.emitAll(repository.getTodoList())
        }
    }

    fun changeShow() {
        showUnchecked = !showUnchecked
        getAllList()
    }

    fun addTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTodoItem(todoItem)
        getAllList()
    }

    fun removeTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoItem(todoItem)
        getAllList()
    }

    fun editStateTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodoItem(todoItem)
        getAllList()
    }

    fun refresh() {
        job?.cancel()
        job =  viewModelScope.async(Dispatchers.IO) {
            async {_result.emit(repository.updateTask())}.await()
        }
    }

    fun logOut(){
        appSharedPreferences.setCurrentToken("")
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}