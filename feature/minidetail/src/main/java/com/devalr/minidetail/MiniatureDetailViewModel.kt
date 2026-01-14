package com.devalr.minidetail

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.extension.isMilestoneAchievable
import com.devalr.domain.extension.recalculateProgress
import com.devalr.domain.extension.toggle
import com.devalr.framework.base.BaseViewModel
import com.devalr.minidetail.interactions.Action
import com.devalr.minidetail.interactions.Action.OnAppear
import com.devalr.minidetail.interactions.Action.OnBackPressed
import com.devalr.minidetail.interactions.Action.OnMilestone
import com.devalr.minidetail.interactions.Action.OnNavigateToEditMiniature
import com.devalr.minidetail.interactions.ErrorType
import com.devalr.minidetail.interactions.Event
import com.devalr.minidetail.interactions.Event.NavigateBack
import com.devalr.minidetail.interactions.Event.NavigateToEditMiniature
import com.devalr.minidetail.interactions.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MiniatureDetailViewModel(
    val miniatureRepository: MiniatureRepository,
    val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> {
                viewModelScope.launch {
                    miniatureRepository.getMiniature(action.miniatureId)
                        .flatMapLatest { miniature ->
                            miniature?.let{
                                projectRepository.getProject(miniature.projectId).map { project ->
                                    Pair(miniature, project)
                                }
                            } ?: emptyFlow()
                        }
                        .catch { error ->
                            updateState { copy(error = ErrorType.RetrievingDatabase) }
                        }.collect { (miniature, project) ->
                            updateState {
                                copy(
                                    miniatureLoaded = true,
                                    miniature = miniature,
                                    parentProject = project,
                                    error = null
                                )
                            }
                        }
                }
            }

            is OnMilestone -> {
                uiState.value.miniature?.let {
                    if (it.completion.isMilestoneAchievable(action.type, action.enable)) {
                        val updatedMiniature =
                            it.copy(completion = it.completion.toggle(action.type))
                                .recalculateProgress()
                        viewModelScope.launch {
                            miniatureRepository.updateMiniature(updatedMiniature)
                            updateState { copy(miniature = updatedMiniature) }
                            projectRepository.updateProjectProgress(updatedMiniature.projectId)
                        }
                    } else {
                        updateState { copy(error = ErrorType.CompletePreviousSteps) }
                    }
                } ?: run {
                    updateState { copy(error = ErrorType.EmptyMiniature) }
                }
            }

            OnBackPressed -> sendEvent(NavigateBack)
            is OnNavigateToEditMiniature -> sendEvent(
                NavigateToEditMiniature(
                    miniatureId = action.miniatureId,
                    projectId = action.projectId
                )
            )
        }
    }
}
