package com.devalr.home

import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.State

class HomeViewModel : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        TODO("Not yet implemented")
    }
}