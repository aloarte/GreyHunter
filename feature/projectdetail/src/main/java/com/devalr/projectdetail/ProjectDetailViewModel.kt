package com.devalr.projectdetail

import androidx.lifecycle.viewModelScope
import com.devalr.framework.base.BaseViewModel
import com.devalr.projectdetail.interactions.Action
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProjectDetailViewModel : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnAppear -> {
                viewModelScope.launch {
                    updateState { copy(miniaturesLoaded = false) }
                    // TODO: Call database and retrieve miniatures list
                    delay(1000)
                    updateState { copy(miniaturesLoaded = true) }

                }
            }

        }
    }
}