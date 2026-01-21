package com.devalr.settings.interactions

sealed interface Event {
    data object NavigateBack : Event
}