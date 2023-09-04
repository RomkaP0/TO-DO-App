package com.romkapo.todoapp.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romkapo.todoapp.R

@Composable
fun DateTodoItemLine(vectorResource: Int, subTitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
        Icon(
            imageVector = ImageVector.vectorResource(id = vectorResource),
            null,
            modifier = Modifier.padding(end = 4.dp),
        )
        Text(text = subTitle, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun ModalSheetImportancePreview() {
    DateTodoItemLine(vectorResource = R.drawable.ic_calendar, subTitle = "26.10.2002 14:30")
}
