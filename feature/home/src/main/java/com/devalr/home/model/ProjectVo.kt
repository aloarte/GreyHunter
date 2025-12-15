package com.devalr.home.model

import com.devalr.domain.model.ProjectBo

sealed interface ProjectVo {
    data class ProjectItem(val project: ProjectBo) : ProjectVo
    data object AddProject : ProjectVo
}
