package com.devalr.createproject.interactions

sealed interface Event {
    data object OnAddedSuccessfully : Event
}