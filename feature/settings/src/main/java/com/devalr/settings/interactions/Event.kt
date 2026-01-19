package com.devalr.settings.interactions

sealed interface Event {
    data object OnNavigateBack : Event
}