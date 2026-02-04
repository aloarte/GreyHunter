package com.devalr.framework.components.add

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType

@Composable
fun AddItemDescription(
    description: String?,
    label: String,
    onChangeDescription: (String) -> Unit
) {
    OutlinedTextField(
        value = description.orEmpty(),
        onValueChange = {
            if (it.length <= 500) {
                onChangeDescription(it)
            }
        },
        singleLine = false,
        label = { GHText(text = label, type = TextType.LabelM) },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}
