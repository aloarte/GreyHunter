package com.devalr.home.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.FRAMEWORK_TOP_BUTTON_SETTINGS
import com.devalr.framework.components.button.GHIconButton
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun AppTitle(onNavigateToSettings: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.Center
        ) {
            GHText(text = "Grey Hunter", type = TextType.Featured)

        }
        GHIconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
            ,
            testTag = FRAMEWORK_TOP_BUTTON_SETTINGS,
            icon = Icons.Default.Settings,
            onButtonClicked = onNavigateToSettings
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTitlePreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppTitle {
                // Do nothing
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTitlePreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppTitle {
                // Do nothing
            }
        }
    }
}