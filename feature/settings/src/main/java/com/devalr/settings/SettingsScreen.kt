package com.devalr.settings

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.settings.composables.SettingsScreenContent
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnBackPressed
import com.devalr.settings.interactions.Action.OnChangeAppearance
import com.devalr.settings.interactions.Event.NavigateBack
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinInject(),
    onBackPressed: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear) }
    Scaffold(
        topBar = {
        }
    ) { innerPadding ->
        SettingsScreenContent(
            innerPadding = innerPadding,
            currentThemeType = state.themeType,
            onBackClicked = { viewModel.onAction(OnBackPressed) },
            onThemeClicked = { viewModel.onAction(OnChangeAppearance(it)) },
            onLanguageClicked = {},
            onImportDataClicked = {},
            onExportDataClicked = {}
        )
    }
}
