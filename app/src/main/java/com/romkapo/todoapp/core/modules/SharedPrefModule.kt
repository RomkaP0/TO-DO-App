package com.romkapo.todoapp.core.modules

import android.content.Context
import android.content.SharedPreferences
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.utils.Constants
import dagger.Module
import dagger.Provides

@Module
interface SharedPrefModule {
    companion object {
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(
                Constants.SHARED_PREF_NAME,
                Context.MODE_PRIVATE,
            )

        @Provides
        fun provideAppSharedPreferences(preferences: SharedPreferences): AppSharedPreferences {
            return AppSharedPreferences(preferences)
        }
    }
}