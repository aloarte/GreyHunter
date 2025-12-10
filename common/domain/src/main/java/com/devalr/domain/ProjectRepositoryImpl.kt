package com.devalr.domain

import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.project.ProjectDao
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ProjectRepositoryImpl(
    private val projectDao: ProjectDao,
    private val miniatureDao: MiniatureDao,
    private val projectDatabaseMapper: Mapper<ProjectEntityData, ProjectBo>
) : ProjectRepository {

    override suspend fun getAllProjects(): Flow<List<ProjectBo>> =
        projectDao.getAllProjects().map { projectEntityList ->
            projectEntityList.map { projectEntity ->
                projectDatabaseMapper.transform(
                    ProjectEntityData(
                        projectEntity = projectEntity,
                        miniatureEntities = emptyList()
                    )
                )
            }
        }

    override suspend fun addProject(project: ProjectBo): Long {
        val projectEntityData = projectDatabaseMapper.transformReverse(project)
        return projectDao.insertProject(projectEntityData.projectEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProject(projectId: Long): Flow<ProjectBo> =
        projectDao.getProjectById(projectId)
            .flatMapLatest { projectEntity ->
                // Return empty flow if the project is not found
                if (projectEntity == null) {
                    return@flatMapLatest kotlinx.coroutines.flow.emptyFlow()
                }
                miniatureDao.getMiniaturesByProject(projectEntity.id)
                    .map { miniatureList ->
                        projectDatabaseMapper.transform(
                            ProjectEntityData(
                                projectEntity = projectEntity,
                                miniatureEntities = miniatureList
                            )
                        )
                    }
            }


    override suspend fun updateProject(project: ProjectBo): Boolean {
        return projectDao.updateProject(projectDatabaseMapper.transformReverse(project).projectEntity) > 0
    }

    override suspend fun deleteProject(projectId: Long) {
        projectDao.deleteProject(projectId)

    }
}