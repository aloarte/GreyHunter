package com.devalr.painting.interactions

sealed interface Event {
    data object NavigateBack : Event
}