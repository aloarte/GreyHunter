package com.devalr.home.interactions

import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.home.model.ProjectVo

data class State(
    val loaded: Boolean = false,
    val projects: List<ProjectVo> = emptyList(),
    val lastUpdatedProject : ProjectBo? = null,
    val lastUpdatedMini : MiniatureBo? = null,
    val error: String? = null
)