package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.domain.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoItemListViewModel @AssistedInject constructor(
    private val repository: MainRepository,
    private val appSharedPreferences: AppSharedPreferences,
    @Assisted private val handle: SavedStateHandle
) :
    ViewModel() {
    private var getDataJob: Job? = null
    private var updateDataJob: Job? = null
    private var _sampleData = MutableStateFlow(TodoScreenStates(emptyList(), emptyList(), true, 0))
    val sampleData: StateFlow<TodoScreenStates> = _sampleData

    init {
        viewModelScope.launch {
            getAllList()
        }

    }

    private fun getAllList() {
        getDataJob?.cancel()
        getDataJob = viewModelScope.launch {
            repository.getTodoList().map {items->
                    _sampleData.update { _sampleData.value.copy(todoFullList = items, countOfCompleted = items.count { it.isComplete })}
                    changeShownList(_sampleData.value.isCheckedShown)
            }.stateIn(viewModelScope)
        }
    }

    fun changeShow() {
        val check = !_sampleData.value.isCheckedShown
        changeShownList(check)
    }

    private fun changeShownList(isChecked: Boolean) {
        _sampleData.update { state ->
            if (isChecked) {
                state.copy(
                    todoList = state.todoFullList,
                    isCheckedShown = isChecked
                )
            } else {
                state.copy(
                    todoList = state.todoFullList.filter { !it.isComplete },
                    isCheckedShown = isChecked
                )
            }
        }
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

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<TodoItemListViewModel>
}
