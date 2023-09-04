package com.romkapo.todoapp.data.model.network

import android.content.SharedPreferences
import com.romkapo.todoapp.utils.Constants.REVISION_KEY
import com.romkapo.todoapp.utils.Constants.SHARED_PREFERENCES_NOTIFICATIONS_IDS
import com.romkapo.todoapp.utils.Constants.SHARED_PREFERENCES_NOTIFICATION_STATUS
import com.romkapo.todoapp.utils.Constants.THEME_KEY
import com.romkapo.todoapp.utils.Constants.TOKEN_KEY
import com.romkapo.todoapp.utils.ThemeMode
import com.romkapo.todoapp.utils.ThemeProvider
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

    fun setTheme(theme: ThemeMode) {
        editor.putInt(THEME_KEY, theme.ordinal)
        editor.apply()

        ThemeProvider.theme.intValue = (theme.ordinal)
    }

    fun getTheme(): Int = sharedPreferences.getInt(THEME_KEY, 2)

    fun putNotificationStatus(status: Boolean) {
        if (status) {
            editor.putString(SHARED_PREFERENCES_NOTIFICATION_STATUS, "yes")
        } else editor.putString(SHARED_PREFERENCES_NOTIFICATION_STATUS, "no")

        editor.apply()
    }

    fun getNotificationStatus(): Boolean? {
        return when (sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATION_STATUS, null)) {
            "yes" -> true
            "no" -> false
            else -> null
        }
    }

    fun addNotification(id: String): String {
        editor.putString(SHARED_PREFERENCES_NOTIFICATIONS_IDS, getNotificationsIds() + " $id")
        editor.apply()
        return sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATIONS_IDS, "").toString()
    }

    fun removeNotification(id: String) {
        val s = getNotificationsIds()
        val arr = ArrayList(s.trim().split(" "))
        if (arr.contains(id)) {
            arr.remove(id)
        }
        val res = arr.fold("") { previous, next -> "$previous $next" }
        editor.putString(SHARED_PREFERENCES_NOTIFICATIONS_IDS, res)
        editor.apply()
    }

    fun getNotificationsIds(): String {
        return sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATIONS_IDS, "").toString()
            .trim()
    }
}
