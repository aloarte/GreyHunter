package com.devalr.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun DataSettings(
    onImportDataClicked: () -> Unit,
    onExportDataClicked: () -> Unit
) {
}

@Preview(showBackground = true)
@Composable
fun DataSettingsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DataSettings(
                onImportDataClicked = {
                    // Do nothing
                },
                onExportDataClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataSettingsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DataSettings(
                onImportDataClicked = {
                    // Do nothing
                },
                onExportDataClicked = {
                    // Do nothing
                }
            )
        }
    }
}
