package com.romkapo.todoapp.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romkapo.todoapp.core.components.app.AppScope
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import javax.inject.Inject

@AppScope
class AuthViewModelFactory @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences,
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(appSharedPreferences) as T
    }
}