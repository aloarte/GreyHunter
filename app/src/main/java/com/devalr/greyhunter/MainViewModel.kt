package com.devalr.greyhunter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _darkModeState = MutableStateFlow(ThemeType.System)
    val darkModeState: StateFlow<ThemeType> = _darkModeState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getAppearanceConfiguration()
                .collect { mode ->
                    _darkModeState.value = mode
                }
        }
    }
}