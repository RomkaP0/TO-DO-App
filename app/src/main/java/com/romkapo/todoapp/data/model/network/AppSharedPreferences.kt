package com.romkapo.todoapp.data.model.network

import android.content.SharedPreferences
import com.romkapo.todoapp.utils.Constants.REVISION_KEY
import com.romkapo.todoapp.utils.Constants.TOKEN_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    private val editor = sharedPreferences.edit()

    fun setCurrentToken(token: String) {
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getCurrentToken(): String? = sharedPreferences.getString(TOKEN_KEY, "")

    fun putRevisionId(revision: Int) {
        editor.putInt(REVISION_KEY, revision)
        editor.apply()
    }

    fun getRevisionId(): Int = sharedPreferences.getInt(REVISION_KEY, 0)
}
