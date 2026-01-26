package com.devalr.projectdetail.interactions

sealed interface Action {
    data class Load(val projectId: Long) : Action
    data class DeleteProject(val projectId: Long) : Action
    data class EditProject(val projectId: Long) : Action
    data object Return : Action
}