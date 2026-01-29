package com.devalr.projectdetail.interactions

import com.devalr.domain.model.ProjectBo

enum class ErrorType {
    RetrievingDatabase,
    Delete
}
data class State(
    val error: Boolean = false,
    val projectLoaded: Boolean = false,
    val project: ProjectBo? = null
)