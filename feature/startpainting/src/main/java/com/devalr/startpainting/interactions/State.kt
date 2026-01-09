package com.devalr.startpainting.interactions

import com.devalr.startpainting.model.StartPaintProjectVo

enum class ErrorType {
    RetrievingDatabase, NoMinisToPaint
}

data class State(
    val projectsLoaded: Boolean = false,
    val projectList: List<StartPaintProjectVo> = emptyList(),
    val paintButtonEnabled: Boolean = false,
    val error: ErrorType? = null
)