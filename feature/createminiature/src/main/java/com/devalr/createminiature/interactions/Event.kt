package com.devalr.createminiature.interactions

sealed interface Event {
    data object OnAddedSuccessfully : Event
}