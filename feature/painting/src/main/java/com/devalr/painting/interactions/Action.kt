package com.devalr.painting.interactions

sealed interface Action {
    data class Load(val minisIds: List<Long>) : Action
    data object Return : Action
    data class FinishPainting(val miniatureIds: List<Long>) : Action
}