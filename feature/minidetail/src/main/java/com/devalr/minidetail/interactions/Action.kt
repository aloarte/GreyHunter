package com.devalr.minidetail.interactions

import com.devalr.domain.enum.MilestoneType


sealed interface Action {
    data class Load(val miniatureId: Long) : Action
    data class UpdateMilestone(val type: MilestoneType, val enable: Boolean) : Action
    data class EditMiniature(val miniatureId: Long, val projectId: Long) : Action
    data class DeleteMiniature(val miniatureId: Long) : Action
    data object Return : Action
}