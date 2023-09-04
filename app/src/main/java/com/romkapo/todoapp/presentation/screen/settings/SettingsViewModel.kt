package com.romkapo.todoapp.presentation.screen.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.domain.MainRepository
import com.romkapo.todoapp.utils.ThemeMode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel @AssistedInject constructor(
    private val repository: MainRepository,
    private val appSharedPreferences: AppSharedPreferences,
    @Assisted private val handle: SavedStateHandle,
) : ViewModel() {

    fun setTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            appSharedPreferences.setTheme(theme = themeMode)
        }
    }

    fun logOut() = viewModelScope.launch(Dispatchers.IO) {
        appSharedPreferences.setCurrentToken("")
        repository.deleteAllTodo()
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<SettingsViewModel>
}
