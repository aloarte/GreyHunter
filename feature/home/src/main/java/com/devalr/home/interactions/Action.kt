package com.devalr.home.interactions

import com.devalr.domain.model.ProjectBo

sealed interface Action {
    data object Load : Action
    data object StartPainting : Action
    data class OpenProjectDetail(val projectId: Long) : Action
    data class OpenMiniatureDetail(val miniatureId: Long) : Action
    data object AddProject : Action
    data class UpdateGamificationMessage(
        val projects: List<ProjectBo>, val almostDoneProjects: List<ProjectBo>
    ) : Action
    data object OpenSettings : Action

}