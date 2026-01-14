package com.devalr.minidetail.interactions

import com.devalr.domain.enum.MilestoneType


sealed interface Action {
    data class OnAppear(val miniatureId: Long) : Action
    data class OnMilestone(val type: MilestoneType, val enable: Boolean) : Action
    data class OnNavigateToEditMiniature(val miniatureId: Long, val projectId: Long) : Action
    data class OnDeleteMiniature(val miniatureId: Long) : Action
    data object OnBackPressed : Action
}