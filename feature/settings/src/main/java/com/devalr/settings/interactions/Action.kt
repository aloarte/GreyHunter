package com.devalr.settings.interactions

import com.devalr.domain.enum.AppearanceType

sealed interface Action {
    data object OnAppear : Action
    data class OnChangeAppearance(val mode: AppearanceType) : Action
}