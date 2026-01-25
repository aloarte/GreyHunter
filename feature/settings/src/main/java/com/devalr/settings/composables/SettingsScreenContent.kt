package com.devalr.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ThemeType
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun SettingsScreenContent(
    innerPadding: PaddingValues = PaddingValues(),
    currentThemeType: ThemeType,
    progressColorType: ProgressColorType,
    appVersion: String,
    onBackClicked: () -> Unit,
    onThemeClicked: (ThemeType) -> Unit,
    onProgressColorClicked: (ProgressColorType) -> Unit,
    onImportDataClicked: () -> Unit,
    onExportDataClicked: () -> Unit,

    ) {
    Column(modifier = Modifier.padding(innerPadding)) {
        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onBackPressed = onBackClicked
        )
        AppSettings(
            currentThemeType = currentThemeType,
            currentProgressColorType = progressColorType,
            onThemeClicked = onThemeClicked,
            onChangeColorClicked = onProgressColorClicked
        )
        DataSettings(
            onImportDataClicked = onImportDataClicked,
            onExportDataClicked = onExportDataClicked
        )
        AppInfo(appVersion = appVersion)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenContentPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsScreenContent(
                currentThemeType = ThemeType.Light,
                progressColorType = ProgressColorType.TrafficLight,
                appVersion = "26.1.0",
                onBackClicked = {
                    // Do nothing
                },
                onThemeClicked = {
                    // Do nothing
                },
                onProgressColorClicked = {
                    // Do nothing
                },
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
fun SettingsScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsScreenContent(
                currentThemeType = ThemeType.Dark,
                progressColorType = ProgressColorType.TrafficLight,
                appVersion = "26.1.0",
                onBackClicked = {
                    // Do nothing
                },
                onThemeClicked = {
                    // Do nothing
                },
                onProgressColorClicked = {
                    // Do nothing
                },
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