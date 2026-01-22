package com.devalr.settings

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ThemeType
import com.devalr.framework.base.BaseViewModel
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnBackPressed
import com.devalr.settings.interactions.Action.OnChangeAppearance
import com.devalr.settings.interactions.Action.OnExportPressed
import com.devalr.settings.interactions.Action.OnImportPressed
import com.devalr.settings.interactions.ErrorType
import com.devalr.settings.interactions.Event
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
            is OnAppear -> onLoadScreen()
            OnBackPressed -> sendEvent(NavigateBack)
            is OnChangeAppearance -> onChangeDarkMode(action.mode)
            is OnImportPressed -> onImportPressed(action.uri)
            is OnExportPressed -> onExportPressed(action.uri)
        }
    }

    private fun onLoadScreen() = viewModelScope.launch {
        combine(
            settingsRepository.getAppearanceConfiguration(),
            settingsRepository.getAppVersion()
        ) { appearanceConfig, appVersion ->
            uiState.value.copy(
                settingsLoaded = true,
                themeType = appearanceConfig,
                appVersion = appVersion
            )
        }
            .catch { updateState { copy(errorType = ErrorType.AppearanceDatastore) } }
            .collect { newState -> updateState { newState } }

    }

    private fun onChangeDarkMode(mode: ThemeType) = viewModelScope.launch {
        settingsRepository.setAppearanceConfiguration(mode)
    }


    private fun onImportPressed(uri: Uri) =
        viewModelScope.launch { settingsRepository.importData(uri) }

    private fun onExportPressed(uri: Uri) = viewModelScope.launch {
        settingsRepository.exportData(uri)
    }


}