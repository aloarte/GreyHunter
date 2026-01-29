package com.devalr.projectdetail

import androidx.lifecycle.viewModelScope
import com.devalr.domain.ProjectRepository
import com.devalr.framework.AppTracer
import com.devalr.framework.base.BaseViewModel
import com.devalr.projectdetail.interactions.Action
import com.devalr.projectdetail.interactions.Action.DeleteProject
import com.devalr.projectdetail.interactions.Action.EditProject
import com.devalr.projectdetail.interactions.Action.Load
import com.devalr.projectdetail.interactions.Action.Return
import com.devalr.projectdetail.interactions.ErrorType
import com.devalr.projectdetail.interactions.ErrorType.Delete
import com.devalr.projectdetail.interactions.ErrorType.RetrievingDatabase
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.Event.LaunchSnackBarError
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import com.devalr.projectdetail.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    private val tracer: AppTracer,
    private val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        tracer.log("ProjectDetailViewModel.onAction: ${action::class.simpleName}")
        when (action) {
            is Load -> loadProject(action.projectId)
            Return -> sendEvent(NavigateBack)
            is EditProject -> sendEvent(NavigateToEditProject(action.projectId))
            is DeleteProject -> deleteProject(action.projectId)
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

    private fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            if (projectRepository.deleteProject(projectId)) {
                sendEvent(NavigateBack)
            } else {
                submitError(Exception("deleteMiniature miniature not deleted"), Delete)
            }
        }
    }

    private fun submitError(error: Throwable, errorType: ErrorType? = null) {
        tracer.recordError(error)
        errorType?.let { sendEvent(LaunchSnackBarError(errorType)) }
    }
}