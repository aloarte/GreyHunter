package com.devalr.settings.interactions

import com.devalr.domain.enum.ThemeType

sealed interface Action {
    data object OnAppear : Action
    data class OnChangeAppearance(val mode: ThemeType) : Action
}