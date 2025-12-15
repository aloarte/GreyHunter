package com.devalr.home.interactions

import com.devalr.home.model.ProjectVo

data class State(
    val projectsLoaded: Boolean = false,
    val projects: List<ProjectVo> = emptyList(),
    val error: String? = null
)