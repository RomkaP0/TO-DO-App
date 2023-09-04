package com.romkapo.todoapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

/* Конвертор timestamp в строку*/
object LongToString {

    @SuppressLint("SimpleDateFormat")
    fun getDateTime(time: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")

            val netDate = Date(time)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}
