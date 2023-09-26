@file:OptIn(ExperimentalMaterial3Api::class)

package com.romkapo.todoapp.ui.screen.addedititem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.DateTimePicker
import com.romkapo.todoapp.utils.LongToString

@Composable()
fun AddEditScreen(
    navController: NavController,
    viewModel: AddEditItemViewModel = daggerViewModel(),

    ) {
    val state = viewModel.currentItemFlow.collectAsState()
    InitialAddEditContent(
        state = state,
        navController = navController,
        saveTodoItem = { viewModel.saveTodoItem() },
        changeText = { viewModel.changeText(it) },
        changeBottomSheetState = { viewModel.changeBottomSheetState() },
        changeStateDialog = { viewModel.changeStateDialog() },
        changeStateDeadline = { viewModel.changeStateDeadline() },
        changeDeadline = { viewModel.changeDeadline(it) },
        changeImportance = { viewModel.changeImportance(it) },
    )
}

@Composable
fun InitialAddEditContent(
    state: State<AddEditScreenStates>,
    navController: NavController,
    saveTodoItem: () -> Unit,
    changeText: (String) -> Unit,
    changeBottomSheetState: () -> Unit,
    changeStateDeadline: () -> Unit,
    changeStateDialog: () -> Unit,
    changeDeadline: (Long) -> Unit,
    changeImportance: (String) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val textColor = animateColorAsState(
        targetValue = if (state.value.isHighlight) Color.Red else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 1000),
    )

    val provideWidthModifier = Modifier.fillMaxWidth()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
    ) {
        Row(modifier = provideWidthModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { navController.navigate("todo_list") }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                    contentDescription = null,
                )
            }
            TextButton(onClick = {
                saveTodoItem()
                navController.navigate("todo_list")
            }) {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(id = R.string.save),
                )
            }
        }
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = provideWidthModifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp),
                )
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight,
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            ),
            minLines = 3,
            maxLines = 3,
            placeholder = { Text(text = "Название события...") },
            value = state.value.text,
            onValueChange = { changeText(it) },
        )

        Column(
            modifier = provideWidthModifier.background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp),
            ),
        ) {
            Row(
                modifier = provideWidthModifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(id = R.string.importance),
                )
                TextButton(onClick = {
                    changeBottomSheetState()
                }) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = state.value.importance,
                        color = textColor.value,
                    )
                }
            }
            Divider(modifier = provideWidthModifier.padding(horizontal = 4.dp))
            Row(
                modifier = provideWidthModifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(id = R.string.complete_before),
                )
                Switch(
                    modifier = Modifier.padding(end = 8.dp),
                    checked = state.value.hasDeadline,
                    onCheckedChange = { changeStateDeadline() },
                )
            }
            if (state.value.hasDeadline) {
                TextButton(onClick = { changeStateDialog() }) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = LongToString.getDateTime(state.value.deadline),
                    )
                }
            }
        }
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val color = if (isPressed) Color.Red else MaterialTheme.colorScheme.secondary
        TextButton(
            modifier = provideWidthModifier.background(
                color,
                shape = RoundedCornerShape(8.dp),
            ),
            interactionSource = interactionSource,
//            colors = ButtonDefaults.buttonColors(containerColor = color),
            enabled = !state.value.isNew,
            onClick = {
                navController.navigate("todo_list?id=${state.value.id}")
            },
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(id = R.string.delete),
            )
        }
    }
    if (state.value.isBottomSheetOpened) {
        ModalBottomSheet(
            onDismissRequest = { changeBottomSheetState() },
            sheetState = bottomSheetState,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { changeImportance("Низкая"); changeBottomSheetState() }.padding(8.dp),
                    text = stringResource(R.string.low),
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { changeImportance("Обычная"); changeBottomSheetState() }.padding(8.dp),
                    text = "Обычная",
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { changeImportance("Высокая"); changeBottomSheetState() }.padding(8.dp),
                    text = "Высокая",
                )
            }
        }
    }
    if (state.value.isDialogShown) {
        DateTimePicker(
            timestamp = state.value.deadline,
            dismiss = { changeStateDialog() },
            saveTime = { time -> changeDeadline(time) },
        )
    }
}

/* Превью не показывает переопределенную тему */
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddEditScreenPreview() {
    Surface {
        MaterialTheme {
            val state = remember {
                mutableStateOf(
                    AddEditScreenStates(
                        id = "",
                        text = "Something",
                        importance = "Обычная",
                        hasDeadline = true,
                        dateCreate = 0L,
                        deadline = System.currentTimeMillis(),
                        isNew = true,
                        isBottomSheetOpened = false,
                        isHighlight = false,
                        isDialogShown = false,
                    ),
                )
            }

            InitialAddEditContent(
                state = state,
                navController = rememberNavController(),
                saveTodoItem = {},
                changeText = {},
                changeBottomSheetState = {},
                changeStateDeadline = {},
                changeStateDialog = {},
                changeDeadline = {},
                changeImportance = {},
            )
        }
    }
}
