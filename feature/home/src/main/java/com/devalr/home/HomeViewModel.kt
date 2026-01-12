package com.devalr.home

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Action.OnAddProject
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.NavigateStartPaint
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.State
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProject
import com.devalr.home.model.ProjectVo.ProjectItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    val projectRepository: ProjectRepository,
    val miniatureRepository: MiniatureRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> {
                initHomeData()
            }

            is OnOpenProjectDetail -> sendEvent(NavigateToProject(projectId = action.projectId))
            is OnStartPainting -> sendEvent(NavigateStartPaint)
            OnAddProject -> sendEvent(NavigateToAddProject)
        }
    }

    private fun initHomeData() {
        viewModelScope.launch {
            combine(
                projectRepository.getAllProjects(),
                projectRepository.getLastUpdatedProject(),
                miniatureRepository.getLastUpdatedMiniature()
            ) { projects, lastProject, lastMini ->
                val voProjects: MutableList<ProjectVo> =
                    projects.map { ProjectItem(it) }.toMutableList()
                voProjects.add(AddProject)

                uiState.value.copy(
                    projects = voProjects,
                    lastUpdatedProject = lastProject,
                    lastUpdatedMini = lastMini,
                    loaded = true,
                    error = null
                )
            }.catch { error ->
                updateState { copy(error = error.message, loaded = true) }
            }.collect { newState ->
                updateState { newState }
            }
        }
    }
}