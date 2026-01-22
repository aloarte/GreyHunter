package com.devalr.greyhunter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : ViewModel() {
    private val _darkModeState = MutableStateFlow(ThemeType.System)
    val darkModeState: StateFlow<ThemeType> = _darkModeState.asStateFlow()

    init {
        viewModelScope.launch {
            val versionName = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
            settingsRepository.setAppVersion(versionName)
            settingsRepository.getAppearanceConfiguration()
                .collect { mode ->
                    _darkModeState.value = mode
                }
        }
    }
}