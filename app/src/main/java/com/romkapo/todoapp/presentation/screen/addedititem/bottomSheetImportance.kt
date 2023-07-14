package com.romkapo.todoapp.presentation.screen.addedititem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalSheetImportance(sheetState: SheetState, dismiss: () -> Unit, onItemClick: (String) -> Unit) {
    ModalBottomSheet(onDismissRequest = { dismiss() }, sheetState = sheetState) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val modifier = Modifier.fillMaxWidth()
            Text(modifier = modifier.clickable { onItemClick("Низкая"); dismiss() }, text = "Низкая")
            Text(modifier = modifier.clickable { onItemClick("Обычная"); dismiss() }, text = "Обычная")
            Text(modifier = modifier.clickable { onItemClick("Высокая"); dismiss() }, text = "Высокая")
        }

    }
}