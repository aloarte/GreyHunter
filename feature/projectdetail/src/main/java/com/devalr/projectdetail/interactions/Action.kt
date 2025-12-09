package com.devalr.projectdetail.interactions

sealed interface Action {
    data object OnAppear : Action
}