package com.romka_po.to_doapp.screen.todolistitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.repository.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemListViewModel @Inject constructor(private val repository: TodoItemsRepository) :
    ViewModel() {
    private val _listTodoItem: MutableLiveData<List<TodoItem>> = MutableLiveData(emptyList())
    val listTodoItem: LiveData<List<TodoItem>> get() = _listTodoItem
    val countOfComplete: MutableLiveData<Int> = MutableLiveData(0)
    var showUnchecked = true

    init {
        viewModelScope.launch {
            repository.todoItems.collect { list ->
                _listTodoItem.value = if (showUnchecked) list else list.filter { !it.isComplete }

                countOfComplete.value = list.filter { it.isComplete }.size
            }
        }

    }

    fun addTodoItemAt(item: TodoItem, position: Int) = viewModelScope.launch {
        repository.addData(item, position)
    }

    fun removeItemWithID(id: String) = viewModelScope.launch {
        repository.removeTodoItemWithID(id)
    }

    fun editTodoItem(item: TodoItem) = viewModelScope.launch {
        repository.editTodoItem(item)
    }

    fun changeShow() {
        showUnchecked = !showUnchecked
        viewModelScope.launch {
            repository.forceEmitData()
        }
    }

    fun forceUpdate() = viewModelScope.launch {
        repository.forceEmitData()
    }

}