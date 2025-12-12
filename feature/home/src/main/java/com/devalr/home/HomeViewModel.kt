package com.devalr.home

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.helpers.alethi
import com.devalr.domain.model.helpers.parshendi
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.LaunchStartPaintModal
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    val miniatureRepository: MiniatureRepository,
    val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> {
                viewModelScope.launch {
                    updateState { copy(projectsLoaded = false) }
                    // TODO: Call database and retrieve the gamification message and the project list
                    //testManageDdbb()
                    observeProjects()
                    updateState { copy(projectsLoaded = true) }

                }
            }

            is OnOpenProjectDetail -> sendEvent(NavigateToProject(projectId = action.projectId))
            is OnStartPainting -> sendEvent(LaunchStartPaintModal)
        }
    }

    private fun observeProjects() {
        viewModelScope.launch {
            projectRepository.getAllProjects()
                .catch { error ->
                    //updateState { copy(errorMessage = error.message) }
                }
                .collect { projectList ->
                    updateState {
                        copy(
                            projectsLoaded = true,
                            projects = projectList,
                            //errorMessage = null
                        )
                    }
                }
        }
    }

    private fun testManageDdbb() {
        viewModelScope.launch {

            val pId = projectRepository.addProject(stormlightArchiveProject)


            miniatureRepository.addMiniature(alethi.copy(projectId = pId))
            miniatureRepository.addMiniature(parshendi.copy(projectId = pId))
            //miniatureRepository.addMiniature(immortal.copy(projectId = pId))
            //miniatureRepository.addMiniature(deathmark.copy(projectId = pId))


        }


    }
}