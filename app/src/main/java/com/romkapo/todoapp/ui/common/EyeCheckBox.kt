package com.romkapo.todoapp.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.romkapo.todoapp.R


@Composable
fun EyeCheckBox(
    isChecked:Boolean,
    onClick: ()->Unit
) {

    val vectorResource = if (isChecked){
        ImageVector.vectorResource(id = R.drawable.show_unchecked_icon)
    }
    else{
        ImageVector.vectorResource(id = R.drawable.hide_unchecked_icon)
    }
    IconButton(onClick = {
        onClick()
    }){
        Icon(imageVector = vectorResource, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun trueEyeButtonPreview() {
    EyeCheckBox(true, {})
}
@Preview(showBackground = true)
@Composable
fun falseEyeButtonPreview() {
    EyeCheckBox(false, {})
}