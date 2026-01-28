package com.devalr.startpainting

import androidx.lifecycle.viewModelScope
import com.devalr.domain.ProjectRepository
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import com.devalr.framework.base.BaseViewModel
import com.devalr.startpainting.interactions.Action
import com.devalr.startpainting.interactions.Action.Load
import com.devalr.startpainting.interactions.Action.Return
import com.devalr.startpainting.interactions.Action.SelectMiniature
import com.devalr.startpainting.interactions.Action.StartPainting
import com.devalr.startpainting.interactions.ErrorType
import com.devalr.startpainting.interactions.Event
import com.devalr.startpainting.interactions.Event.LaunchErrorSnackBar
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigateToPaintMiniatures
import com.devalr.startpainting.interactions.State
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StartPaintingViewModel(
    private val tracer: AppTracer,
    private val projectRepository: ProjectRepository,
    private val projectVoMapper: Mapper<ProjectBo, StartPaintProjectVo>,
    private val miniatureVoMapper: Mapper<MiniatureBo, StartPaintMiniatureVo>
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        tracer.log("StartPaintingViewModel.onAction: ${action::class.simpleName}")
        when (action) {
            is Load -> loadProjects()
            Return -> sendEvent(NavigateBack)
            is SelectMiniature -> selectMiniature(miniature = action.miniature)
            is StartPainting -> startPainting()
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            projectRepository.getAllProjects()
                .catch { error -> submitError(error, ErrorType.RetrievingDatabase) }
                .collect { projects ->
                    updateState {
                        copy(
                            projectList = projects
                                .filter { it.minis.isNotEmpty() }
                                .map { projectBo ->
                                    projectVoMapper.transform(projectBo)
                                },
                            projectsLoaded = true
                        )
                    }
                }
        }
    }

    private fun selectMiniature(miniature: StartPaintMiniatureVo) {
        val updatedProjectList = uiState.value.projectList.map { project ->
            if (project.id != miniature.projectId) {
                project
            } else {
                project.copy(
                    minis = project.minis.map { mini ->
                        if (mini.id != miniature.id) {
                            mini
                        } else {
                            mini.copy(isSelected = !mini.isSelected)
                        }
                    }
                )
            }
        }

        updateState {
            copy(
                projectList = updatedProjectList,
                paintButtonEnabled = updatedProjectList.any { it.minis.any { it.isSelected } }
            )
        }
    }

    private fun startPainting() {
        val selectedMiniatures = uiState.value.projectList.flatMap { project ->
            project.minis.filter { it.isSelected }
        }
        if (selectedMiniatures.isNotEmpty()) {
            val miniatureBoList = selectedMiniatures.map { miniatureVo ->
                miniatureVoMapper.transformReverse(miniatureVo)
            }
            sendEvent(NavigateToPaintMiniatures(miniatureBoList.map { it.id }))

        } else {
            submitError(
                Exception("startPainting. Button submited no minis to paint"),
                ErrorType.NoMinisToPaint
            )
        }
    }

    private fun submitError(error: Throwable, errorType: ErrorType? = null) {
        tracer.recordError(error)
        errorType?.let { sendEvent(LaunchErrorSnackBar(errorType)) }
    }

}