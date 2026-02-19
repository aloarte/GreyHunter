package com.devalr.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.enums.ProgressColorType
import com.devalr.domain.enums.ThemeType
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun SettingsScreenContent(
    innerPadding: PaddingValues = PaddingValues(),
    currentThemeType: ThemeType,
    progressColorType: ProgressColorType,
    appVersion: String,
    onNavigateBack: () -> Unit,
    onChangeTheme: (ThemeType) -> Unit,
    onChangeProgressColor: (ProgressColorType) -> Unit,
    onImportProjects: () -> Unit,
    onExportProjects: () -> Unit
) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        item {
            TopButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onNavigateBack = onNavigateBack
            )
        }
        item {
            AppSettings(
                currentThemeType = currentThemeType,
                currentProgressColorType = progressColorType,
                onChangeTheme = onChangeTheme,
                onChangeProgressColor = onChangeProgressColor
            )
        }
        item {
            DataSettings(
                onImportProjects = onImportProjects,
                onExportProjects = onExportProjects
            )
        }
        item { AppInfo(appVersion = appVersion) }
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
                onNavigateBack = {
                    // Do nothing
                },
                onChangeTheme = {
                    // Do nothing
                },
                onChangeProgressColor = {
                    // Do nothing
                },
                onImportProjects = {
                    // Do nothing
                },
                onExportProjects = {
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
                onNavigateBack = {
                    // Do nothing
                },
                onChangeTheme = {
                    // Do nothing
                },
                onChangeProgressColor = {
                    // Do nothing
                },
                onImportProjects = {
                    // Do nothing
                },
                onExportProjects = {
                    // Do nothing
                }
            )
        }
    }
}