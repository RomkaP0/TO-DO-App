@file:OptIn(ExperimentalMaterial3Api::class)

package com.romkapo.todoapp.presentation.screen.addedititem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.ModalSheetImportance
import com.romkapo.todoapp.utils.DateTimePicker
import com.romkapo.todoapp.utils.LongToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun AddEditScreen(
    navController: NavController,
    viewModel: AddEditItemViewModel = daggerViewModel(),

    ) {
    val state = viewModel.currentItemFlow.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val textColor = animateColorAsState(
        targetValue = if (state.value.isHighlight) Color.Red else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 1000)
    )


    val provideWidthModifier = Modifier.fillMaxWidth()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {

        Row(modifier = provideWidthModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                    contentDescription = null
                )
            }
            TextButton(onClick = {
                viewModel.saveTodoItem()
                navController.navigate("todo_list")
            }) {
                Text(style = MaterialTheme.typography.labelMedium, text = stringResource(id = R.string.save))
            }
        }
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = provideWidthModifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                )
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            ),
            minLines = 3,
            maxLines = 3,
            placeholder = { Text(text = "Название события...") },
            value = state.value.text,
            onValueChange = { viewModel.changeText(it) })

        Column(modifier = provideWidthModifier.background(color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(8.dp))) {
            Row(
                modifier = provideWidthModifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodyMedium, text = stringResource(id = R.string.importance))
                TextButton(onClick = {
                    viewModel.changeBottomSheetState()
                }) {
                    Text(style = MaterialTheme.typography.bodyMedium, text = state.value.importance, color = textColor.value)
                }
            }
            Divider(modifier = provideWidthModifier.padding(horizontal = 4.dp))
            Row(
                modifier = provideWidthModifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodyMedium, text = stringResource(id = R.string.complete_before))
                Switch(modifier = Modifier.padding(end = 8.dp),
                    checked = state.value.hasDeadline,
                    onCheckedChange = { viewModel.changeStateDeadline() })
            }
            if (state.value.hasDeadline) {
                TextButton(onClick = { viewModel.changeStateDialog() }) {
                    Text(style = MaterialTheme.typography.bodyMedium, text = LongToString.getDateTime(state.value.deadline))
                }
            }
        }
        TextButton(
            modifier = provideWidthModifier.background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp)
            ),
            enabled = !state.value.isNew,
            onClick = {
                viewModel.removeTodoItem()
                navController.navigate("todo_list")
            }) {
            Text(style = MaterialTheme.typography.bodyMedium, text = stringResource(id = R.string.delete))
        }
    }
    if (state.value.isBottomSheetOpened) {
        ModalSheetImportance(
            sheetState = bottomSheetState,
            dismiss = { viewModel.changeBottomSheetState() },
            onItemClick = { value -> viewModel.changeImportance(value) })
    }
    if (state.value.isDialogShown) {
        DateTimePicker(
            timestamp = state.value.deadline,
            dismiss = { viewModel.changeStateDialog() },
            saveTime = { time -> viewModel.changeDeadline(time) })
    }
}

//@Preview
//@Composable
//fun previewAddEditScreen(){
//    AddEditScreen(viewModel = AddEditItemViewModel(), navController = rememberNavController())
//}