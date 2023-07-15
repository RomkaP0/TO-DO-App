package com.romkapo.todoapp.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.RadioTextRow
import com.romkapo.todoapp.ui.common.WhiteRoundColumn
import com.romkapo.todoapp.utils.Tet
import com.romkapo.todoapp.utils.ThemeMode


@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = daggerViewModel()
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        WhiteRoundColumn(modifier = Modifier.fillMaxWidth()) {
            RadioTextRow(
                state = Tet.theme.intValue == ThemeMode.LIGHT.ordinal,
                text = "Светлая тема"
            ) {
                viewModel.setTheme(ThemeMode.LIGHT)
            }
            RadioTextRow(
                state = Tet.theme.intValue == ThemeMode.DARK.ordinal,
                text = "Темная тема"
            ) {
                viewModel.setTheme(ThemeMode.DARK)
            }
            RadioTextRow(
                state = Tet.theme.intValue == ThemeMode.SYSTEM.ordinal,
                text = "Системная тема"
            ) {
                viewModel.setTheme(ThemeMode.SYSTEM)
            }
        }
    }
}