package com.devalr.settings

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ThemeType
import com.devalr.framework.base.BaseViewModel
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.Load
import com.devalr.settings.interactions.Action.Return
import com.devalr.settings.interactions.Action.ChangeAppearance
import com.devalr.settings.interactions.Action.ChangeProgressColors
import com.devalr.settings.interactions.Action.ExportProjects
import com.devalr.settings.interactions.Action.ImportProjects
import com.devalr.settings.interactions.ErrorType
import com.devalr.settings.interactions.Event
import com.devalr.settings.interactions.Event.LaunchSnackBar
import com.devalr.settings.interactions.Event.NavigateBack
import com.devalr.settings.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
    val settingsRepository: SettingsRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is Load -> onLoadScreen()
            Return -> sendEvent(NavigateBack)
            is ChangeAppearance -> changeDarkMode(action.mode)
            is ChangeProgressColors -> changeProgressColors(action.colorType)
            is ImportProjects -> importProjects(action.uri)
            is ExportProjects -> exportProjects(action.uri)
        }
    }

    private fun onLoadScreen() = viewModelScope.launch {
        combine(
            settingsRepository.getAppearanceConfiguration(),
            settingsRepository.getProgressColorConfiguration(),
            settingsRepository.getAppVersion()
        ) { appearanceConfig, progressColorConfig, appVersion ->
            uiState.value.copy(
                settingsLoaded = true,
                themeType = appearanceConfig,
                progressColorConfigType = progressColorConfig,
                appVersion = appVersion
            )
        }
            .catch { updateState { copy(errorType = ErrorType.AppearanceDatastore) } }
            .collect { newState -> updateState { newState } }

    }

    private fun changeDarkMode(mode: ThemeType) = viewModelScope.launch {
        settingsRepository.setAppearanceConfiguration(mode)
    }

    private fun changeProgressColors(colorType: ProgressColorType) = viewModelScope.launch {
        settingsRepository.setProgressColorConfiguration(colorType)
    }

    private fun importProjects(uri: Uri) =
        viewModelScope.launch {
            if (settingsRepository.importData(uri)) {
                sendEvent(LaunchSnackBar(true, SnackBarType.SUCCESS))
            } else {
                sendEvent(LaunchSnackBar(true, SnackBarType.ERROR))
            }
        }

    private fun exportProjects(uri: Uri) = viewModelScope.launch {
        if (settingsRepository.exportData(uri)) {
            sendEvent(LaunchSnackBar(false, SnackBarType.SUCCESS))
        } else {
            sendEvent(LaunchSnackBar(false, SnackBarType.ERROR))
        }
    }

}