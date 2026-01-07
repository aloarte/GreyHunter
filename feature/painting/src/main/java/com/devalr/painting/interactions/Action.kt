package com.devalr.painting.interactions

sealed interface Action {
    data class OnAppear(val minisIds: List<Long>) : Action
}