package com.devalr.home.interactions

import com.devalr.domain.model.ProjectBo

data class State(
    val projectsLoaded: Boolean = false,
    val projects: List<ProjectBo> = emptyList()
)