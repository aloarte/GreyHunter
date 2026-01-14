package com.devalr.home.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme


@Composable
fun StartPaint(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GHText(
            text = "Pick one or more miniatures and start painting to upgrade your project progress!",
            type = TextType.Description
        )
        Spacer(modifier = Modifier.height(10.dp))
        GHButton(text = "Start painting", onClick = onClick)
    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            StartPaint {
                // Do nothing
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            StartPaint {
                // Do nothing
            }
        }
    }
}
