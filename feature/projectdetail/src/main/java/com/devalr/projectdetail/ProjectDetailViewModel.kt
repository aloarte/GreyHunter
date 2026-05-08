package com.devalr.projectdetail

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.framework.AppTracer
import com.devalr.framework.base.BaseViewModel
import com.devalr.projectdetail.interactions.Action
import com.devalr.projectdetail.interactions.Action.DeleteProject
import com.devalr.projectdetail.interactions.Action.EditProject
import com.devalr.projectdetail.interactions.Action.Load
import com.devalr.projectdetail.interactions.Action.Return
import com.devalr.projectdetail.interactions.Action.SortMiniature
import com.devalr.projectdetail.interactions.ErrorType
import com.devalr.projectdetail.interactions.ErrorType.Delete
import com.devalr.projectdetail.interactions.ErrorType.RetrievingDatabase
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.Event.LaunchSnackBarError
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import com.devalr.projectdetail.interactions.State
import com.devalr.projectdetail.model.SortDirection
import com.devalr.projectdetail.model.SortDirection.Down
import com.devalr.projectdetail.model.SortDirection.Up
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    private val tracer: AppTracer,
    private val projectRepository: ProjectRepository,
    private val miniatureRepository: MiniatureRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        tracer.log("ProjectDetailViewModel.onAction: ${action::class.simpleName}")
        when (action) {
            is DeleteProject -> deleteProject(action.projectId)
            is EditProject -> sendEvent(NavigateToEditProject(action.projectId))
            is Load -> loadProject(action.projectId)
            Return -> sendEvent(NavigateBack)
            is SortMiniature -> sortMiniature(action.order, action.miniId)
        }
    }

    private fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            if (projectRepository.deleteProject(projectId)) {
                sendEvent(NavigateBack)
            } else {
                submitError(Exception("deleteMiniature miniature not deleted"), Delete)
            }
        }
    }

    private fun loadProject(projectId: Long) {
        viewModelScope.launch {
            projectRepository.getProject(projectId)
                .catch { error ->
                    updateState { copy(error = true) }
                    submitError(error, RetrievingDatabase)
                }
                .collect { project ->
                    updateState {
                        copy(
                            projectLoaded = true,
                            project = project,
                        )
                    }
                }
        }
    }

    private fun sortMiniature(direction: SortDirection, miniId: Long) {
        val minis = uiState.value.project?.minis
            ?.sortedBy { it.sortOrder }
            ?.toMutableList()
            ?: return

        val currentMini = minis.firstOrNull { it.id == miniId } ?: return

        val neighbour = when (direction) {
            Up -> minis.takeWhile { it.id != miniId }.lastOrNull()
            Down -> minis.dropWhile { it.id != miniId }.drop(1).firstOrNull()
        } ?: return

        viewModelScope.launch {
            miniatureRepository.updateMiniature(currentMini.copy(sortOrder = neighbour.sortOrder))
            miniatureRepository.updateMiniature(neighbour.copy(sortOrder = currentMini.sortOrder))
            updateState { copy(animatedMiniIds = currentMini.id to neighbour.id) }
        }
    }

    private fun submitError(error: Throwable, errorType: ErrorType? = null) {
        tracer.recordError(error)
        errorType?.let { sendEvent(LaunchSnackBarError(errorType)) }
    }
}