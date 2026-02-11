package com.devalr.home

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import com.devalr.framework.base.BaseViewModel
import com.devalr.home.interactions.Action
import com.devalr.home.interactions.Action.AddProject
import com.devalr.home.interactions.Action.Load
import com.devalr.home.interactions.Action.OpenMiniatureDetail
import com.devalr.home.interactions.Action.OpenProjectDetail
import com.devalr.home.interactions.Action.OpenSettings
import com.devalr.home.interactions.Action.StartPainting
import com.devalr.home.interactions.Action.UpdateGamificationMessage
import com.devalr.home.interactions.ErrorType.RetrievingDatabase
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.LaunchSnackBarError
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToMiniature
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.Event.NavigateToSettings
import com.devalr.home.interactions.Event.NavigateToStartPaint
import com.devalr.home.interactions.State
import com.devalr.home.model.GamificationMessageType.AlmostDone
import com.devalr.home.model.GamificationMessageType.EmptyProjects
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.GamificationMessageType.ProgressRange
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProjectItem
import com.devalr.home.model.ProjectVo.ProjectItem
import com.devalr.home.model.ProjectsStats
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val tracer: AppTracer,
    val projectRepository: ProjectRepository,
    val miniatureRepository: MiniatureRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        tracer.log("HomeViewModel.onAction: ${action::class.simpleName}")
        when (action) {
            is Load -> initHomeData(action.bigScreen)
            is OpenProjectDetail -> sendEvent(NavigateToProject(projectId = action.projectId))
            is OpenMiniatureDetail -> sendEvent(NavigateToMiniature(miniatureId = action.miniatureId))
            is StartPainting -> sendEvent(NavigateToStartPaint)
            is UpdateGamificationMessage -> updateGamificationMessage(
                projects = action.projects,
                almostDoneProjects = action.almostDoneProjects
            )

            OpenSettings -> sendEvent(NavigateToSettings)
            AddProject -> sendEvent(NavigateToAddProject)
        }
    }

    private fun initHomeData(bigScreen:Boolean) = viewModelScope.launch {
        val columnNumber = if(bigScreen) 3 else 2
        combine(
            projectRepository.getAllProjects(),
            projectRepository.getAlmostDoneProjects(columnNumber),
            miniatureRepository.getLastUpdatedMiniatures(columnNumber)
        ) { projects, almostDoneProjects, lastMinis ->
            val voProjects: MutableList<ProjectVo> =
                projects.map { ProjectItem(it) }.toMutableList()
            voProjects.add(AddProjectItem)
            onAction(UpdateGamificationMessage(projects, almostDoneProjects))
            uiState.value.copy(
                projects = voProjects,
                almostDoneProjects = almostDoneProjects,
                lastUpdatedMinis = lastMinis,
                stats = calculateProjectStats(projects),
                loaded = true
            )
        }.catch { error ->
            tracer.recordError(error)
            sendEvent(LaunchSnackBarError(RetrievingDatabase))
            updateState { copy(error = true) }
        }.collect { newState ->
            updateState { newState }
        }
    }

    private fun calculateProjectStats(projects: List<ProjectBo>): ProjectsStats = ProjectsStats(
        totalProgress = if (projects.isNotEmpty()) {
            projects.sumOf { it.progress.toDouble() }.div(projects.size).times(100).toInt()
        } else 0,
        minisFinished = projects.sumOf { it.minis.count { it.percentage == 1f } },
        totalMinis = projects.sumOf { it.minis.size },
        projectsFinished = projects.count { it.progress == 1f },
        totalProjects = projects.size
    )

    fun updateGamificationMessage(
        projects: List<ProjectBo>,
        almostDoneProjects: List<ProjectBo>
    ) {
        val gamificationMessage = if (almostDoneProjects.isNotEmpty()) {
            AlmostDone(almostDoneProjects.first().name)
        } else if (projects.isNotEmpty()) {
            val progressAverage = projects
                //.removeOutliers()
                .map { it.progress }
                .average()
                .toFloat()
            when (progressAverage) {
                0f -> EmptyProjects
                in 0.01f..0.2f -> ProgressRange(0.2f)
                in 0.2f..0.5f -> ProgressRange(0.5f)
                in 0.5f..0.7f -> ProgressRange(0.7f)
                in 0.7f..0.9f -> ProgressRange(0.9f)
                in 0.9f..0.99f -> ProgressRange(0.99f)
                1f -> ProgressRange(1f)
                else -> None
            }
        } else {
            None
        }
        updateState { copy(gamificationSentence = gamificationMessage) }
    }

    fun List<ProjectBo>.removeOutliers(): List<ProjectBo> {
        val sorted = this.map { it.progress }.sorted()
        val median = sorted[sorted.size / 2]
        return filter { it.progress >= median * 0.5f }
    }
}