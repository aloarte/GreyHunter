package com.devalr.projectdetail

import androidx.lifecycle.viewModelScope
import com.devalr.domain.ProjectRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.projectdetail.interactions.Action
import com.devalr.projectdetail.interactions.Action.OnAppear
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> observeProject(action.projectId)
        }
    }

    private fun observeProject(projectId: Long) {
        viewModelScope.launch {
            projectRepository.getProject(projectId)
                .catch { error ->
                    updateState { copy(error = error.message) }
                }
                .collect { project ->
                    updateState {
                        copy(
                            projectLoaded = true,
                            project = project,
                            error = null
                        )
                    }
                }
        }
    }
}