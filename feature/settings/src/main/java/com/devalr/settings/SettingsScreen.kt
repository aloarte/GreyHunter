package com.devalr.settings

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.settings.composables.SettingsScreenContent
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Event.OnNavigateBack
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
                OnNavigateBack -> onBackPressed()
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
            onBackClicked = onBackPressed,
            onThemeClicked = { viewModel.onAction(Action.OnChangeAppearance(it)) },
            onLanguageClicked = {},
            onImportDataClicked = {},
            onExportDataClicked = {}
        )
    }
}
