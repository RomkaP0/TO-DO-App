package com.romkapo.todoapp.ui.screen.todolistitems

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.local.TodoItem
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.domain.repository.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoItemListViewModel @AssistedInject constructor(
    private val repository: MainRepository,
    @Assisted private val handle: SavedStateHandle,
) : ViewModel() {
    private var getDataJob: Job? = null
    private var updateDataJob: Job? = null
    private var _sampleData = MutableStateFlow(TodoScreenStates(emptyList(), emptyList(), true, 0))
    val sampleData: StateFlow<TodoScreenStates> = _sampleData
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val message = mutableStateOf("")
    private var deletedTodoItem:TodoItem? = null

    private var job: Job? = null
    var id: String? = handle["id"]

    init {
        viewModelScope.launch {
            getAllList()
        }
    }

    private fun getAllList() {
        getDataJob?.cancel()
        getDataJob = viewModelScope.launch {
            repository.getTodoList().map { items ->
                _sampleData.update {
                    _sampleData.value.copy(
                        todoFullList = items,
                        countOfCompleted = items.count { it.isComplete },
                    )
                }
                changeShownList(_sampleData.value.isCheckedShown)
                if (id != null) {
                    val item = items.find { it.id == id }
                    item?.let { showSnackBar(it) }
                    id = null
                }
                _isRefreshing.emit(false)
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
                    isCheckedShown = isChecked,
                )
            } else {
                state.copy(
                    todoList = state.todoFullList.filter { !it.isComplete },
                    isCheckedShown = isChecked,
                )
            }
        }
    }

    fun addTodoItem() = viewModelScope.launch(Dispatchers.IO) {
        message.value = ""
        job?.cancel()
        deletedTodoItem?.let { repository.addTodoItem(it) }
    }

    private fun removeTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTodoItem(todoItem)
    }

    fun editStateTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTodoItem(todoItem)
    }

    fun refresh() {
        updateDataJob?.cancel()
        updateDataJob = viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.emit(true)
            repository.fetchTasks()
        }
    }

    fun showSnackBar(item: TodoItem) = viewModelScope.launch {
        val named = mutableIntStateOf(5)
        job?.let {
            if (it.isActive) {
                message.value = ""
                job!!.cancel()
            }
        }
        job = viewModelScope.launch {
            deletedTodoItem = item
            removeTodoItem(item)
            while (job!!.isActive) {
                message.value = "${named.intValue} Удаление ${item.text}"
                delay(1000L)
                named.intValue -= 1
                if (named.intValue == 0) {
                    message.value = ""
                    job!!.cancel()
                }
            }
        }
    }

    override fun onCleared() {
        getDataJob?.cancel()
        super.onCleared()
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<TodoItemListViewModel>
}
