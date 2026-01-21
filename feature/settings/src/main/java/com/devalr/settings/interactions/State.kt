package com.devalr.settings.interactions

import com.devalr.domain.enum.AppearanceType


enum class ErrorType {
    AppearanceDatastore
}

data class State(
    val settingsLoaded: Boolean = false,
    val errorType: ErrorType? = null,
    val appearanceType: AppearanceType = AppearanceType.System
)