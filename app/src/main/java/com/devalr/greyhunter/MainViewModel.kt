package com.devalr.greyhunter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ProgressColorType.Brand
import com.devalr.domain.enum.ThemeType
import com.devalr.domain.enum.ThemeType.System
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : ViewModel() {
    private val _darkModeState = MutableStateFlow(System)
    val darkModeState: StateFlow<ThemeType> = _darkModeState.asStateFlow()

    private val _colorState = MutableStateFlow(Brand)
    val colorState: StateFlow<ProgressColorType> = _colorState.asStateFlow()

    init {
        viewModelScope.launch {
            val versionName = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
            settingsRepository.setAppVersion(versionName)
            combine(
                settingsRepository.getAppearanceConfiguration(),
                settingsRepository.getProgressColorConfiguration()
            ) { appearanceConfig, progressColorConfig ->
                appearanceConfig to progressColorConfig
            }.collect { (appearanceConfig, progressColorConfig) ->
                _darkModeState.value = appearanceConfig
                _colorState.value = progressColorConfig
            }
        }
    }

}