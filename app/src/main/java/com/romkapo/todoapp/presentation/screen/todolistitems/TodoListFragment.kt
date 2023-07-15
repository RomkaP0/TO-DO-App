@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.ui.common.EyeCheckBox
import com.romkapo.todoapp.ui.common.TodoListItem
import com.romkapo.todoapp.ui.theme.Typography

@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoItemListViewModel = daggerViewModel(),
) {
    val state = viewModel.sampleData.collectAsState()


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                onClick = { navController.navigate("add_edit") })
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize(),

    )
    {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.my_tasks),
                    style = Typography.titleLarge
                )
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(style = MaterialTheme.typography.labelMedium, text = stringResource(id = R.string.complete) + " " + state.value.countOfCompleted)
                EyeCheckBox(isChecked = state.value.isCheckedShown) {
                    viewModel.changeShow()
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(24.dp)),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top)
            ) {
                items(
                    items = state.value.todoList,
                    key = { listItem ->
                        listItem.id
                    }) { item ->
                    val dismissState = rememberDismissState(confirmValueChange = {
                        viewModel.removeTodoItem(item)
                        true
                    })
                    SwipeToDismiss(
                        directions = setOf(DismissDirection.StartToEnd),
                        modifier = Modifier.animateItemPlacement(),
                        state = dismissState,
                        background = {},

                        dismissContent = {
                            TodoListItem(item,
                                onCheckChanged = {
                                    viewModel.editStateTodoItem(item.copy(isComplete = !item.isComplete))
                                },
                                onItemClicked = {
                                    navController.navigate("add_edit?id=${item.id}")
                                })
                        })
                }
            }
        }
    }
}



