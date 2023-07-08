package com.romkapo.todoapp.presentation.screen.addedititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.domain.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditItemViewModel @Inject constructor(
    private val repository: MainRepository,
    private val coroutineScope: CoroutineScope
) : ViewModel() {
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

    fun addTodoItem(todoItem: TodoItem) = coroutineScope.launch(Dispatchers.IO) {
        repository.addTodoItem(todoItem)
    }

    fun removeTodoItem(todoItem: TodoItem) = coroutineScope.launch(Dispatchers.IO) {
        repository.deleteTodoItem(todoItem)
    }

    fun editTodoItem(todoItem: TodoItem) = coroutineScope.launch(Dispatchers.IO) {
        repository.updateTodoItem(todoItem)
    }
}
