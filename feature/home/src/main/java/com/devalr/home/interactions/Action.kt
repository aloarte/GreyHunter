package com.devalr.home.interactions

import com.devalr.domain.model.ProjectBo

sealed interface Action {

    data object OnAppear : Action
    data object OnStartPainting : Action
    data class OnOpenProjectDetail(val projectId: Long) : Action
    data class OnOpenMiniatureDetail(val miniatureId: Long) : Action
    data object OnAddProject : Action
    data class OnUploadGamificationMessage(
        val projects: List<ProjectBo>, val almostDoneProjects: List<ProjectBo>
    ) : Action
    data object OnOpenSettings : Action

}