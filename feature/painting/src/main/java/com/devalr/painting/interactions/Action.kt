package com.devalr.painting.interactions

sealed interface Action {
    data class OnAppear(val minisIds: List<Long>) : Action
    data object OnBackPressed : Action
    data class OnDonePainting(val miniatureIds: List<Long>) : Action
}