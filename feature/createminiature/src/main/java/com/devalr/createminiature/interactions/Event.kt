package com.devalr.createminiature.interactions

sealed interface Event {
    data object NavigateBack : Event
    data class LaunchSnackBarError(val error: ErrorType) :Event
}