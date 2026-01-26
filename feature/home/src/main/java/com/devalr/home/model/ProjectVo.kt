package com.devalr.home.model

import com.devalr.domain.model.ProjectBo

sealed interface ProjectVo {

    fun hasMinis(): Boolean

    data class ProjectItem(val project: ProjectBo) : ProjectVo {
        override fun hasMinis(): Boolean = project.minis.isNotEmpty()
    }

    data object AddProjectItem : ProjectVo {
        override fun hasMinis(): Boolean = false
    }
}
