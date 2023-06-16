package com.romka_po.to_doapp.screen.todoaddedititem

import androidx.lifecycle.ViewModel
import com.romka_po.to_doapp.model.TodoItem
import com.romka_po.to_doapp.repository.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditItemViewModel @Inject constructor(private val repository: TodoItemsRepository):ViewModel() {
    var completeTimeStamp = System.currentTimeMillis()

    fun addTodoItem(item: TodoItem){
        repository.addData(item)
    }

    fun editTodoItem(item: TodoItem){
        repository.editTodoItem(item)
    }
    fun removeItemWithID(id:String){
        repository.removeTodoItemWithID(id)
    }
}