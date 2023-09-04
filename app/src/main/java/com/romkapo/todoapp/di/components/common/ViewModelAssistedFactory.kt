package com.romkapo.todoapp.di.components.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface ViewModelAssistedFactory<T : ViewModel> {
    fun create(handle: SavedStateHandle): T
}
