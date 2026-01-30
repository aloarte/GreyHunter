package com.devalr.home.model

import com.devalr.domain.model.ProjectBo

sealed interface ProjectVo {

    fun hasMinis(): Boolean
    fun hasUnfinishedMinis(): Boolean

    data class ProjectItem(val project: ProjectBo) : ProjectVo {
        override fun hasMinis(): Boolean = project.minis.isNotEmpty()
        override fun hasUnfinishedMinis(): Boolean = project.minis.any { it.percentage < 1f }
    }

    data object AddProjectItem : ProjectVo {
        override fun hasMinis(): Boolean = false
        override fun hasUnfinishedMinis() = false
    }
}
