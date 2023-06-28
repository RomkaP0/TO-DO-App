package com.romka_po.to_doapp.screen.todoaddedititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.repository.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditItemViewModel @Inject constructor(private val repository: TodoItemsRepository):ViewModel() {
    var completeTimeStamp = System.currentTimeMillis()
    var id = ""

    fun addTodoItem(item: TodoItem)= viewModelScope.launch{
        repository.addData(item)
    }

    fun editTodoItem(item: TodoItem) = viewModelScope.launch{
        repository.editTodoItem(item)
    }
    fun removeItemWithID(id:String)=viewModelScope.launch{
        repository.removeTodoItemWithID(id)
    }
}