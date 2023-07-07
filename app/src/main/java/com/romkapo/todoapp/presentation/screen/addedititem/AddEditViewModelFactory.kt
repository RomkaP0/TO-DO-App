package com.romkapo.todoapp.presentation.screen.addedititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.core.components.app.AppScope
import com.romkapo.todoapp.domain.MainRepository
import javax.inject.Inject

@AppScope
class AddEditViewModelFactory @Inject constructor(
    private val repository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditItemViewModel(repository) as T
    }
}