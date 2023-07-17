package com.romkapo.todoapp.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RadioTextRow(state:Boolean, text:String, onClick:()->Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), verticalAlignment = Alignment.CenterVertically){
        RadioButton(selected = state, onClick = { onClick()})
        Text(style = MaterialTheme.typography.bodyMedium, text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun radioTextRowPreview() {
    RadioTextRow(state = false, text = "fgge", {})
}
@Preview(showBackground = true)
@Composable
fun checkRadioTextRowPreview() {
    RadioTextRow(state = true, text = "fgge", {})
}