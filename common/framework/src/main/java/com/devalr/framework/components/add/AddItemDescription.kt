package com.devalr.framework.components.add

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType

@Composable
fun AddItemDescription(
    description: String?,
    label: String,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = description.orEmpty(),
        onValueChange = onDescriptionChange,
        singleLine = false,
        label = { GHText(text = label, type = TextType.LabelM) },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}

@Composable
fun AddItemName(
    name: String?, label: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name.orEmpty(),
        onValueChange = onNameChange,
        singleLine = true,
        label = { GHText(text = label, type = TextType.LabelM) },
        modifier = Modifier.fillMaxWidth()
    )
}