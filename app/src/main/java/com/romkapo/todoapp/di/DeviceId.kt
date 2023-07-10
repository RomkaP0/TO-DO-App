package com.romkapo.todoapp.di

import android.provider.Settings

/*Предоставляет ID устройства*/
data class DeviceId(val id: String = Settings.Secure.ANDROID_ID)
