package com.devalr.createproject

import com.devalr.createproject.interactions.Action
import com.devalr.createproject.interactions.Event
import com.devalr.createproject.interactions.State
import com.devalr.domain.ProjectRepository
import com.devalr.framework.base.BaseViewModel

class AddProjectViewModel(val projectRepository: ProjectRepository) :
    BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
    }

}