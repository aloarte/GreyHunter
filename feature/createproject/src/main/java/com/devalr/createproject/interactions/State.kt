package com.devalr.createproject.interactions

import com.devalr.domain.model.ProjectBo

enum class ErrorType {
    EmptyTitle,
    AddDatabase,
    BadId,
    EditDatabase,
    ImportImage
}

data class State(
    val error: Boolean = false,
    val projectName: String? = null,
    val projectDescription: String? = null,
    val projectImage: String? = null,
    val projectToUpdate: ProjectBo? = null,
    val editMode: Boolean = false
)