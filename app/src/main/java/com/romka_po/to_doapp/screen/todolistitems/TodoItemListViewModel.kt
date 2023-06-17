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
class TodoItemListViewModel @Inject constructor(private val repository: TodoItemsRepository) : ViewModel() {
    private val _liveData: MutableLiveData<List<TodoItem>> = MutableLiveData(emptyList())
    val liveData: LiveData<List<TodoItem>> get() = _liveData
    val countOfComplete: MutableLiveData<Int> = MutableLiveData(0)

    init {
        viewModelScope.launch {
            repository.todoItems.collect { list ->
                _liveData.value = list
                countOfComplete.value = list.filter { it.isComplete }.size
            }
        }

    }

    fun addTodoItemAt(item: TodoItem, position: Int) {
        repository.addData(item, position)
    }

    fun removeItemWithID(id: String) {
        repository.removeTodoItemWithID(id)
    }

    fun editTodoItem(item: TodoItem) {
        repository.editTodoItem(item)
    }

}