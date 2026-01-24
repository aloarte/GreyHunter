package com.devalr.settings.interactions

import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ThemeType


enum class ErrorType {
    AppearanceDatastore
}

data class State(
    val settingsLoaded: Boolean = false,
    val errorType: ErrorType? = null,
    val themeType: ThemeType = ThemeType.System,
    val progressColorConfigType: ProgressColorType = ProgressColorType.Brand,
    val appVersion: String = ""
)