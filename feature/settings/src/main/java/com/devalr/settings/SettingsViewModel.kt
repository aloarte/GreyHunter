package com.devalr.settings

import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.DarkModeType
import com.devalr.framework.base.BaseViewModel
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnChangeDarkMode
import com.devalr.settings.interactions.ErrorType
import com.devalr.settings.interactions.Event
import com.devalr.settings.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingsViewModel(
    val projectRepository: SettingsRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> onLoadScreen()
            is OnChangeDarkMode -> onChangeDarkMode(action.mode)
        }
    }

    private fun onChangeDarkMode(mode: DarkModeType) {
        viewModelScope.launch {
            projectRepository.setDarkModeConfiguration(mode)
            // Force
        }
    }

    private fun onLoadScreen() {
        viewModelScope.launch {
            projectRepository.getDarkModeConfiguration()
                .catch { updateState { copy(errorType = ErrorType.DarkModeDatastore) } }
                .collect { updateState { copy(darkMode = it) } }
        }
    }

}