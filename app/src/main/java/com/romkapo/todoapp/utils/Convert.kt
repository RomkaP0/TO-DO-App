package com.romkapo.todoapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Convert {
    @SuppressLint("SimpleDateFormat")
    fun getDateTime(time: Long): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(time)
            val result = sdf.format(netDate)
            result
        } catch (e: Exception) {
            e.toString()
        }
    }
}