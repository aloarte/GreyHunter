package com.devalr.minidetail.interactions

sealed interface Action {
    data class OnAppear(val miniatureId: Long) : Action
}