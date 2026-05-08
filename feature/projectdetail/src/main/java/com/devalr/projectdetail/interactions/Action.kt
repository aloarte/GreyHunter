package com.devalr.projectdetail.interactions

import com.devalr.projectdetail.model.SortDirection

sealed interface Action {
    data class Load(val projectId: Long) : Action
    data class DeleteProject(val projectId: Long) : Action
    data class EditProject(val projectId: Long) : Action

    data class SortMiniature(val order: SortDirection, val miniId: Long) : Action
    data object Return : Action
}