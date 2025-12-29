package com.devalr.createproject.interactions

import com.devalr.createproject.model.MiniatureVo
import com.devalr.domain.model.ProjectBo


enum class ErrorType {
    EmptyTitle,
    AddDatabase,
    BadId,
    ImageImport,
    EditDatabase
}

data class State(
    val projectMinis: List<MiniatureVo>? = null,
    val projectName: String? = null,
    val projectDescription: String? = null,
    val errorType: ErrorType? = null,
    val projectImage: String? = null,
    val projectToUpdate: ProjectBo? = null,
    val editMode: Boolean = false
)