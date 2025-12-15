package com.devalr.home

import androidx.lifecycle.viewModelScope
import com.devalr.domain.ProjectRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnAddProject
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.LaunchStartPaintModal
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.State
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProject
import com.devalr.home.model.ProjectVo.ProjectItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> {
                viewModelScope.launch {
                    updateState { copy(projectsLoaded = false) }
                    // TODO: Call database and retrieve the gamification message and the project list
                    observeProjects()
                    updateState { copy(projectsLoaded = true) }

                }
            }

            is OnOpenProjectDetail -> sendEvent(NavigateToProject(projectId = action.projectId))
            is OnStartPainting -> sendEvent(LaunchStartPaintModal)
            OnAddProject -> sendEvent(NavigateToAddProject)
        }
    }

    private fun observeProjects() {
        viewModelScope.launch {
            projectRepository.getAllProjects()
                .catch { error ->
                    updateState { copy(error = error.message) }
                }
                .collect { projectList ->
                    val voProjects: MutableList<ProjectVo> =
                        projectList.map { ProjectItem(it) }.toMutableList()
                    voProjects.add(AddProject)
                    updateState {
                        copy(
                            projectsLoaded = true,
                            projects = voProjects,
                            error = null
                        )
                    }
                }
        }
    }
}