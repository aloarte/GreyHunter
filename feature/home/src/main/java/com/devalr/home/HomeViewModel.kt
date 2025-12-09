package com.devalr.home

import androidx.lifecycle.viewModelScope
import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when(action){
            is OnAppear -> {
                viewModelScope.launch {
                    updateState { copy(projectsLoaded = false) }
                    // TODO: Call database and retrieve the gamification message and the project list
                    delay(1000)
                    updateState { copy(projectsLoaded = true) }

                }
            }
            is OnOpenProjectDetail -> TODO()
            is OnStartPainting -> TODO()
        }
    }
}