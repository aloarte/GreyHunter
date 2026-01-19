package com.devalr.settings.interactions

import com.devalr.domain.enum.DarkModeType

sealed interface Action {
    data object OnAppear : Action
    data class OnChangeDarkMode(val mode: DarkModeType) : Action
}