package com.devalr.framework.components.add

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType


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