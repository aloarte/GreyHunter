package com.devalr.minidetail.interactions

import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo

enum class ErrorType{
    RetrievingDatabase,
    CompletePreviousSteps,
    EmptyMiniature
}
data class State(
    val miniatureLoaded: Boolean = false,
    val miniature: MiniatureBo? = null,
    val parentProject: ProjectBo? = null,
    val error: ErrorType? = null

)