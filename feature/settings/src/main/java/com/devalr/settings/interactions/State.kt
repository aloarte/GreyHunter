package com.devalr.settings.interactions

import com.devalr.domain.enum.DarkModeType


enum class ErrorType {
    DarkModeDatastore
}

data class State(
    val settingsLoaded: Boolean = false,
    val errorType: ErrorType? = null,
    val darkMode: DarkModeType = DarkModeType.System
)