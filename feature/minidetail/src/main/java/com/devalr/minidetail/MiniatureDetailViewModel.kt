package com.devalr.minidetail

import androidx.lifecycle.viewModelScope
import com.devalr.framework.base.BaseViewModel
import com.devalr.minidetail.interactions.Action
import com.devalr.minidetail.interactions.Event
import com.devalr.minidetail.interactions.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MiniatureDetailViewModel : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnAppear -> {
                viewModelScope.launch {
                    updateState { copy(miniatureLoaded = false) }
                    // TODO: Call database and retrieve miniatures list
                    delay(1000)
                    updateState { copy(miniatureLoaded = true) }
                }
            }
        }
    }
}