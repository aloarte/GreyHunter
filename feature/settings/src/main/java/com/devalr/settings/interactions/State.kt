package com.devalr.settings.interactions

import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ThemeType

enum class ErrorType {
    DatastoreRetrieval,
    Import,
    Export
}

enum class OperationType {
    Import,
    Export
}

data class State(
    val settingsLoaded: Boolean = false,
    val themeType: ThemeType = ThemeType.System,
    val progressColorConfigType: ProgressColorType = ProgressColorType.Brand,
    val appVersion: String = ""
)