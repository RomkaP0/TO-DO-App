package com.romkapo.todoapp.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(timestamp: Long, dismiss: () -> Unit, saveTime: (Long) -> Unit) {
    val isDatePicker = remember {
        mutableStateOf(true)
    }
    val datePickerState = rememberDatePickerState(timestamp)
    val timePickerState = rememberTimePickerState(
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        ).hour,
        LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).minute
    )
    Dialog(onDismissRequest = { dismiss() },    properties = DialogProperties(
        usePlatformDefaultWidth = false
    )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (isDatePicker.value) {
                    DatePicker(modifier = Modifier.requiredWidth(360.dp), state = datePickerState)
                } else {
                    TimePicker(modifier = Modifier, state = timePickerState)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { isDatePicker.value = !isDatePicker.value }) {
                        Icon(
                            imageVector = if (isDatePicker.value) Icons.Default.AccessTime else Icons.Default.CalendarToday,
                            contentDescription = null
                        )
                    }
                    TextButton(modifier = Modifier.weight(1f, true), onClick = { dismiss() }) {
                        Text(text = "Отменить")
                    }
                    TextButton(modifier = Modifier.weight(1f, true), onClick = {
                        saveTime(datePickerState.selectedDateMillis!! + (timePickerState.hour * 3600000L) + (timePickerState.minute * 60000L)-TimeZone.getDefault().rawOffset)
                        dismiss()
                    }) {
                        Text(text = "Сохранить")
                    }
                }
            }
        }
    }
}