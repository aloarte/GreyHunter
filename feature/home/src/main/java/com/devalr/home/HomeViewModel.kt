package com.devalr.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.LaunchStartPaintModal
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.State
import kotlinx.coroutines.delay
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
                    delay(1000)
                    testManageDdbb()
                    updateState { copy(projectsLoaded = true) }

                }
            }

            is OnOpenProjectDetail -> sendEvent(NavigateToProject(projectId = action.projectId))
            is OnStartPainting -> sendEvent(LaunchStartPaintModal)
        }
    }

    private fun testManageDdbb() {
        viewModelScope.launch {

            val pId = projectRepository.addProject(ProjectBo(name = "Tyranids"))


            miniatureRepository.addMiniature(
                MiniatureBo(projectId = pId, name = "Mini 1")
            )
            miniatureRepository.addMiniature(
                MiniatureBo(projectId = pId, name = "Mini 2")
            )
            miniatureRepository.addMiniature(
                MiniatureBo(projectId = pId, name = "Mini 3")
            )
            miniatureRepository.getMiniaturesFromProject(pId).collect { mini ->
                mini.forEach {
                    Log.d("ALRALR", "Mini: $it")
                }
            }

        }


    }
}