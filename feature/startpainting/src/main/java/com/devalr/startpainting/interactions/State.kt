package com.devalr.startpainting.interactions

import com.devalr.startpainting.model.StartPaintProjectVo

enum class ErrorType {
    RetrievingDatabase, NoMinisToPaint
}

data class State(
    val error: Boolean = false,
    val projectsLoaded: Boolean = false,
    val projectList: List<StartPaintProjectVo> = emptyList(),
    val paintButtonEnabled: Boolean = false
)