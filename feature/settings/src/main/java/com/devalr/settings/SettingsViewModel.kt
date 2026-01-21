package com.devalr.settings

import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ThemeType
import com.devalr.framework.base.BaseViewModel
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnChangeAppearance
import com.devalr.settings.interactions.ErrorType
import com.devalr.settings.interactions.Event
import com.devalr.settings.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingsViewModel(
    val settingsRepository: SettingsRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> onLoadScreen()
            is OnChangeAppearance -> onChangeDarkMode(action.mode)
        }
    }

    private fun onChangeDarkMode(mode: ThemeType) {
        viewModelScope.launch {
            settingsRepository.setAppearanceConfiguration(mode)
        }
    }

    private fun onLoadScreen() {
        viewModelScope.launch {
            settingsRepository.getAppearanceConfiguration()
                .catch { updateState { copy(errorType = ErrorType.AppearanceDatastore) } }
                .collect {
                    updateState { copy(themeType = it) }
                }
        }
    }

}