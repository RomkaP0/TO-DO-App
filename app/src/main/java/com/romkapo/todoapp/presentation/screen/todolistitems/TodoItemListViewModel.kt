package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radusalagean.infobarcompose.InfoBarMessage
import com.radusalagean.infobarcompose.InfoBarSlideEffect
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.domain.MainRepository
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
    @Assisted private val handle: SavedStateHandle
) :
    ViewModel() {
    private var getDataJob: Job? = null
    private var updateDataJob: Job? = null
    private var _sampleData = MutableStateFlow(TodoScreenStates(emptyList(), emptyList(), true, 0))
    val sampleData: StateFlow<TodoScreenStates> = _sampleData
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    var message: InfoBarMessage? by mutableStateOf(null)
    var isFirstSnack: Boolean by mutableStateOf(true)
    val infoBarSlideEffect: InfoBarSlideEffect by derivedStateOf {
        if (isFirstSnack)
            InfoBarSlideEffect.FROM_BOTTOM
        else
            InfoBarSlideEffect.NONE
    }


    var job: Job? = null
        private set
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
                        countOfCompleted = items.count { it.isComplete })
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

    private fun addTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTodoItem(todoItem)
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
        isFirstSnack = true
        job?.let {
            if (it.isActive) {
                job!!.cancel()
                message = null
            }
        }
        job = viewModelScope.launch {
            removeTodoItem(item)
            while (job!!.isActive) {
                message = InfoBarMessage(
                    text = "${named.intValue} Удаление ${item.text}",
                    displayTimeSeconds = 1,
                    action = "Return",
                    onAction = {
                        addTodoItem(item)
                        message=null
                        job!!.cancel()
                    }
                )
                delay(1000L)
                named.intValue -= 1
                if (named.intValue == 0) {
                    message = null
                    isFirstSnack = false
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
