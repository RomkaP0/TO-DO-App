package com.romkapo.todoapp.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.RadioTextRow
import com.romkapo.todoapp.utils.ThemeMode
import com.romkapo.todoapp.utils.ThemeProvider


@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = daggerViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
    ) {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            RadioTextRow(
                state = ThemeProvider.theme.intValue == ThemeMode.LIGHT.ordinal,
                text = "Светлая тема"
            ) {
                viewModel.setTheme(ThemeMode.LIGHT)
            }
            RadioTextRow(
                state = ThemeProvider.theme.intValue == ThemeMode.DARK.ordinal,
                text = "Темная тема"
            ) {
                viewModel.setTheme(ThemeMode.DARK)
            }
            RadioTextRow(
                state = ThemeProvider.theme.intValue == ThemeMode.SYSTEM.ordinal,
                text = "Системная тема"
            ) {
                viewModel.setTheme(ThemeMode.SYSTEM)
            }
        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                ),
            onClick = {
                viewModel.logOut()
                navController.navigateUp()
            }) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(id = R.string.logout)
            )
        }
    }
}