package com.devalr.painting.interactions

import com.devalr.domain.model.MiniatureBo


enum class ErrorType {
    RetrievingDatabase
}

data class State(
    val minisLoaded: Boolean = false,
    val miniatures: List<MiniatureBo> = emptyList(),
    val error: ErrorType? = null

)