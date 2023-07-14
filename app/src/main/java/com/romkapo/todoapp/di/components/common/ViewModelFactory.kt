package com.romkapo.todoapp.di.components.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/* Фабрика вьюмоделей */

interface ViewModelFactory {
    fun <VM : ViewModel> create(modelClass: Class<VM>, handle: SavedStateHandle): VM
}