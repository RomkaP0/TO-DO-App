package com.romkapo.todoapp.ui.screen.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.data.network.AppSharedPreferences
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuthViewModel @AssistedInject constructor(
    private val appSharedPreferences: AppSharedPreferences,
    @Assisted private val handle: SavedStateHandle,
) : ViewModel() {
    val token = appSharedPreferences.getCurrentToken()
    fun putToken(token: String) {
        appSharedPreferences.setCurrentToken(token)
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AuthViewModel>
}
