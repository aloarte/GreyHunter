package com.devalr.framework.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun GHButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(50.dp),
        enabled = enabled,
        shape = RoundedCornerShape(30),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onClick
    ) {
        GHText(text = text.uppercase(), type = TextType.Title)
    }
}


@Preview(showBackground = true)
@Composable
private fun GHButtonPreviews() {
    GreyHunterTheme {
        Column {
            GHButton(text = "Retry", onClick = {})
            VerticalDivider(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", onClick = {})
        }
    }
}