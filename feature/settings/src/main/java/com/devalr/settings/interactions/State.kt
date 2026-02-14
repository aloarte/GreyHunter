package com.devalr.settings.interactions

import com.devalr.domain.enums.ProgressColorType
import com.devalr.domain.enums.ThemeType

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
    val error: Boolean = false,
    val settingsLoaded: Boolean = false,
    val themeType: ThemeType = ThemeType.System,
    val progressColorConfigType: ProgressColorType = ProgressColorType.Brand,
    val appVersion: String = ""
)