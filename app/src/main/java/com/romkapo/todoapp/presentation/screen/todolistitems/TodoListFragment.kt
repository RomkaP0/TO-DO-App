@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
)

package com.romkapo.todoapp.presentation.screen.todolistitems

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
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
    val lazyColumnState = rememberLazyListState()
    val refreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refresh() })

    val elevation = remember {
        derivedStateOf {
            lazyColumnState.firstVisibleItemIndex != 0
        }
    }
    val alpha: Dp by animateDpAsState(if (elevation.value) 16.dp else 0.dp, label = "")

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    onClick = {
                        navController.navigate("add_edit")
                    },
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(it),
            ) {
                Column(
                    modifier = Modifier
                        .shadow(alpha)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.my_tasks),
                            style = Typography.titleLarge,
                        )
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = stringResource(id = R.string.complete) + " " + state.value.countOfCompleted,
                        )
                        EyeCheckBox(isChecked = state.value.isCheckedShown) {
                            viewModel.changeShow()
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                        .clip(shape = RoundedCornerShape(24.dp)),
                    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
                    state = lazyColumnState,
                ) {
                    items(
                        items = state.value.todoList,
                        key = { listItem ->
                            listItem.id
                        },
                    ) { item ->
                        val dismissState = rememberDismissState(confirmValueChange = {
                            if (it == DismissValue.DismissedToEnd) {
                                viewModel.showSnackBar(item)
                                true
                            } else {
                                false
                            }
                        },
                            positionalThreshold = {250.dp.value}
                        )
                        SwipeToDismiss(
                            directions = setOf(DismissDirection.StartToEnd),
                            modifier = Modifier.animateItemPlacement(),
                            state = dismissState,
                            background = {},

                            dismissContent = {
                                TodoListItem(
                                    item,
                                    onCheckChanged = {
                                        viewModel.editStateTodoItem(item.copy(isComplete = !item.isComplete))
                                    },
                                    onItemClicked = {
                                        navController.navigate("add_edit?id=${item.id}")
                                    },
                                )
                            },
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        if (viewModel.message.value != "") {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .shadow(elevation = 8.dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = viewModel.message.value,
                    color = MaterialTheme.colorScheme.primary,
                )
                TextButton(onClick = { viewModel.addTodoItem() }) {
                    Text(text = "Вернуть")
                }
            }
        }
    }
}
