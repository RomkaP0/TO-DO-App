package com.romkapo.todoapp.presentation.screen.auth

import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.data.model.network.AppSharedPreferences

class AuthViewModel(
    private val appSharedPreferences: AppSharedPreferences,
) : ViewModel() {
    val token = appSharedPreferences.getCurrentToken()
    fun putToken(token: String) {
        appSharedPreferences.setCurrentToken(token)
    }
}
