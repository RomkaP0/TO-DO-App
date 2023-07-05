package com.romkapo.todoapp.presentation.screen.todoaddedititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditItemViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {
    var completeTimeStamp = System.currentTimeMillis()
    var id = ""

    private var _currentItemFlow = MutableStateFlow(TodoItem())
    var currentItemFlow = _currentItemFlow.asStateFlow()
    var currentItem = TodoItem()


    fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTodoItem(id)?.let { _currentItemFlow.emit(it) }
        }
    }

    fun addTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTodoItem(todoItem)
    }

    fun removeTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoItem(todoItem)
    }

    fun editTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodoItem(todoItem)
    }
}