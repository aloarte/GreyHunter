package com.devalr.domain.mappers

import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectEntity
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData

class ProjectMapper(val miniatureMapper: Mapper<MiniatureEntity, MiniatureBo>) :
    Mapper<ProjectEntityData, ProjectBo>() {

    override fun transform(data: ProjectEntityData): ProjectBo = ProjectBo(
        id = data.projectEntity.id,
        description = data.projectEntity.description,
        name = data.projectEntity.name,
        imageUri = data.projectEntity.imageUri,
        progress = data.projectEntity.completionPercentage,
        minis = data.miniatureEntities.map { miniatureEntity ->
            miniatureMapper.transform(miniatureEntity)
        }
    )

    override fun transformReverse(data: ProjectBo): ProjectEntityData = ProjectEntityData(
        projectEntity = ProjectEntity(
            id = data.id,
            name = data.name,
            description = data.description,
            imageUri = data.imageUri,
            completionPercentage = data.progress
        ),
        miniatureEntities = data.minis.map { miniatureBo ->
            miniatureMapper.transformReverse(miniatureBo)
        }

    )
}