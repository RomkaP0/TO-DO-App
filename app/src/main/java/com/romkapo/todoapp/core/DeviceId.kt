package com.romkapo.todoapp.core

import android.provider.Settings

data class DeviceId(val id: String = Settings.Secure.ANDROID_ID)
