package com.romkapo.todoapp.ui.screen.addedititem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.local.TodoItem
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.domain.repository.MainRepository
import com.romkapo.todoapp.utils.Importance
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddEditItemViewModel @AssistedInject constructor(
    private val repository: MainRepository,
    private val coroutineScope: CoroutineScope,
    @Assisted private val handle: SavedStateHandle,
) : ViewModel() {

    init {
        handle.get<String>("id")?.let {
            loadTask(it)
        }
    }

    private var _currentItemFlow =
        MutableStateFlow(
            AddEditScreenStates(
                id = UUID.randomUUID().toString(),
                text = "",
                importance = "Обычная",
                hasDeadline = false,
                dateCreate = 0L,
                deadline = System.currentTimeMillis(),
                isNew = true,
                isBottomSheetOpened = false,
                isHighlight = false,
                isDialogShown = false,
            ),
        )
    var currentItemFlow: StateFlow<AddEditScreenStates> = _currentItemFlow.asStateFlow()

    fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTodoItem(id)?.let {
                _currentItemFlow.value = _currentItemFlow.value.copy(
                    id = it.id,
                    text = it.text,
                    importance = when (it.importance) {
                        Importance.LOW -> "Низкая"
                        Importance.MEDIUM -> "Обычная"
                        Importance.HIGH -> "Высокая"
                    },
                    dateCreate = it.dateCreate,
                    hasDeadline = it.dateComplete != null,
                    deadline = it.dateComplete ?: System.currentTimeMillis(),
                    isNew = false,
                    isHighlight = it.importance == Importance.HIGH,
                )
                showHighlight(it.importance == Importance.HIGH)
            }
        }
    }

    fun saveTodoItem() = coroutineScope.launch(Dispatchers.IO) {
        if (_currentItemFlow.value.isNew) {
            repository.addTodoItem(getTodoItemFromState())
        } else {
            repository.updateTodoItem(getTodoItemFromState())
        }
    }

    fun getTodoItemFromState(): TodoItem {
        val state = _currentItemFlow.value
        return TodoItem(
            id = state.id,
            text = state.text,
            importance = when (state.importance) {
                "Низкая" -> Importance.LOW
                "Обычная" -> Importance.MEDIUM
                "Высокая" -> Importance.HIGH
                else -> throw IllegalArgumentException("Неизвестная важность дела")
            },
            dateCreate = if (state.dateCreate == 0L) System.currentTimeMillis() else state.dateCreate,
            dateComplete = if (state.hasDeadline) state.deadline else null,
            dateEdit = if (state.isNew) null else System.currentTimeMillis(),
        )
    }

    fun changeText(string: String) {
        _currentItemFlow.value = _currentItemFlow.value.copy(text = string)
    }

    fun changeStateDeadline() {
        _currentItemFlow.value =
            _currentItemFlow.value.copy(hasDeadline = !_currentItemFlow.value.hasDeadline)
    }

    fun changeDeadline(timeStamp: Long) {
        _currentItemFlow.value =
            _currentItemFlow.value.copy(deadline = timeStamp)
    }

    fun changeBottomSheetState() {
        _currentItemFlow.value =
            _currentItemFlow.value.copy(isBottomSheetOpened = !_currentItemFlow.value.isBottomSheetOpened)
    }

    fun changeImportance(importance: String) {
        val highlightState = importance == "Высокая"
        _currentItemFlow.value =
            _currentItemFlow.value.copy(
                importance = importance,
                isHighlight = highlightState,
            )
        showHighlight(highlightState)
    }

    fun showHighlight(highlightState: Boolean) {
        if (highlightState) {
            viewModelScope.launch {
                delay(1500L)
                _currentItemFlow.value =
                    _currentItemFlow.value.copy(
                        isHighlight = false,
                    )
            }
        }
    }

    fun changeStateDialog() {
        _currentItemFlow.value =
            _currentItemFlow.value.copy(
                isDialogShown = !_currentItemFlow.value.isDialogShown,
            )
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AddEditItemViewModel>
}
