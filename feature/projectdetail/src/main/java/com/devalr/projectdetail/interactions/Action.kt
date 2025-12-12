package com.devalr.projectdetail.interactions

sealed interface Action {
    data class OnAppear(val projectId: Long) : Action
}