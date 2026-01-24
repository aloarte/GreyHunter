package com.devalr.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.devalr.settings.composables.SettingsScreenContent
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnBackPressed
import com.devalr.settings.interactions.Action.OnChangeAppearance
import com.devalr.settings.interactions.Action.OnChangeProgressColors
import com.devalr.settings.interactions.Action.OnExportPressed
import com.devalr.settings.interactions.Action.OnImportPressed
import com.devalr.settings.interactions.Event.NavigateBack
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinInject(),
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.onAction(OnImportPressed(it))
        }
    }
    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            viewModel.onAction(OnExportPressed(it))
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
            progressColorType = state.progressColorConfigType,
            appVersion = state.appVersion,
            onBackClicked = { viewModel.onAction(OnBackPressed) },
            onThemeClicked = { viewModel.onAction(OnChangeAppearance(it)) },
            onProgressColorClicked = { viewModel.onAction(OnChangeProgressColors(it)) },
            onImportDataClicked = {
                importLauncher.launch(arrayOf("*/*"))
            },
            onExportDataClicked = {
                exportLauncher.launch("backup_projects.csv")
            }
        )
    }
}
