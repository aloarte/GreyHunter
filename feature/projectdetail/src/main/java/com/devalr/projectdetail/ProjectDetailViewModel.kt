package com.devalr.projectdetail

import androidx.lifecycle.viewModelScope
import com.devalr.domain.ProjectRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.projectdetail.interactions.Action
import com.devalr.projectdetail.interactions.Action.OnAppear
import com.devalr.projectdetail.interactions.Action.OnBackPressed
import com.devalr.projectdetail.interactions.Action.OnDeleteProject
import com.devalr.projectdetail.interactions.Action.OnNavigateToEditProject
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import com.devalr.projectdetail.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> loadProject(action.projectId)
            OnBackPressed -> sendEvent(NavigateBack)
            is OnNavigateToEditProject -> sendEvent(NavigateToEditProject(action.projectId))
            is OnDeleteProject -> deleteProject(action.projectId)
        }
    }

    private fun loadProject(projectId: Long) {
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

    private fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            if (projectRepository.deleteProject(projectId)) {
                sendEvent(NavigateBack)
            } else {
                //TODO: Handle error
            }
        }
    }
}