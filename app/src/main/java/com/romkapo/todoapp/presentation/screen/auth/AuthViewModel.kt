package com.romkapo.todoapp.presentation.screen.auth

import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences,
) : ViewModel() {

    fun putToken(token: String) {
        appSharedPreferences.setCurrentToken(token)
    }
}