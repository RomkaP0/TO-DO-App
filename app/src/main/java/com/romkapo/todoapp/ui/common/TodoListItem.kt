package com.romkapo.todoapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romkapo.todoapp.R
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.utils.Importance
import com.romkapo.todoapp.utils.LongToString

@Composable
fun TodoListItem(todoItem: TodoItem, onCheckChanged: () -> Unit, onItemClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { onItemClicked() },
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Checkbox(
            checked = todoItem.isComplete,
            onCheckedChange = {
                onCheckChanged()
            },
            modifier = Modifier.size(48.dp),
        )
        when (todoItem.importance) {
            Importance.LOW -> Icon(
                painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                modifier = Modifier.width(8.dp),
            )

            Importance.HIGH -> Icon(
                painterResource(id = R.drawable.ic_warning),
                contentDescription = null,
                modifier = Modifier.width(8.dp),
            )

            else -> Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f, true),
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = todoItem.text,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            DateTodoItemLine(
                vectorResource = R.drawable.ic_calendar,
                subTitle = LongToString.getDateTime(todoItem.dateCreate),
            )
            todoItem.dateEdit?.let {
                DateTodoItemLine(
                    vectorResource = R.drawable.edit_note_fill0_wght400_grad0_opsz20,
                    subTitle = LongToString.getDateTime(it),
                )
            }
            todoItem.dateComplete?.let {
                DateTodoItemLine(
                    vectorResource = R.drawable.outline_done_all_24,
                    subTitle = LongToString.getDateTime(it),
                )
            }
        }
        Icon(
            painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
        )
    }
}

@Preview
@Composable
fun TodoListItemPreview() {
    Column {
        TodoListItem(
            todoItem = TodoItem(
                "",
                "eggggggggggggggggggggggggggggg\nggggggggggggg\ngggggggggggggggggggggggggggggggggggggggggggg",
                Importance.HIGH,
                System.currentTimeMillis(),
                dateComplete = System.currentTimeMillis(),
                System.currentTimeMillis(),
                true,
            ),
            {},
            {},
        )
        TodoListItem(
            todoItem = TodoItem(
                "",
                "egggggggggggggggggggggggggggggggggggggggggg",
                Importance.HIGH,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
            ),
            {},
            {},
        )
    }
}
