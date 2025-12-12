package com.devalr.projectdetail.interactions

import com.devalr.domain.model.ProjectBo

data class State(
    val projectLoaded: Boolean = false,
    val project: ProjectBo? = null,
    val error: String? = null
)