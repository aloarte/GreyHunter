package com.devalr.minidetail.interactions

import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo

enum class ErrorType {
    RetrievingDatabase,
    CompletePreviousSteps,
    EmptyMiniature,
    Delete
}

data class State(
    val miniatureLoaded: Boolean = false,
    val error: Boolean = false,
    val miniature: MiniatureBo? = null,
    val parentProject: ProjectBo? = null
)