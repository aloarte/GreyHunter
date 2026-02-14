package com.devalr.data.database

import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectEntity

data class ProjectEntityData(
    val projectEntity: ProjectEntity,
    val miniatureEntities: List<MiniatureEntity>
)