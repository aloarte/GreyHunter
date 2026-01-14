@file:OptIn(ExperimentalCoroutinesApi::class)

package com.devalr.domain

import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectDao
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Clock

class ProjectRepositoryImpl(
    private val projectDao: ProjectDao,
    private val miniatureDao: MiniatureDao,
    private val projectDatabaseMapper: Mapper<ProjectEntityData, ProjectBo>,
    private val clock: Clock = Clock.systemDefaultZone()
) : ProjectRepository {

    override suspend fun getAllProjects(): Flow<List<ProjectBo>> =
        projectDao.getAllProjects().flatMapLatest { projectEntities ->
            if (projectEntities.isEmpty()) {
                return@flatMapLatest flowOf(emptyList())
            }
            val miniatureFlows: List<Flow<List<MiniatureEntity>>> = projectEntities.map { project ->
                miniatureDao.getMiniaturesByProject(project.id)
            }

            combine(miniatureFlows) { arrayOfMiniatureLists ->
                projectEntities.mapIndexed { index, project ->
                    val miniatures = arrayOfMiniatureLists[index]
                    projectDatabaseMapper.transform(
                        ProjectEntityData(
                            projectEntity = project,
                            miniatureEntities = miniatures
                        )
                    )
                }
            }
        }

    override suspend fun getProject(projectId: Long): Flow<ProjectBo?> =
        projectDao.getProjectById(projectId)
            .flatMapLatest { projectEntity ->
                if (projectEntity == null) {
                    return@flatMapLatest flowOf(null)
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

    override suspend fun getLastUpdatedProject(): Flow<ProjectBo?> =
        projectDao.getLastUpdatedProject().flatMapLatest { projectEntity ->
            if (projectEntity == null) {
                return@flatMapLatest flowOf(null)
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

    override suspend fun getAlmostDoneProjects(projectsNumber: Int): Flow<List<ProjectBo>> =
        projectDao.getAlmostDoneProjects(projectsNumber).map { projectList ->
            projectList.map {
                projectDatabaseMapper.transform(
                    ProjectEntityData(
                        projectEntity = it,
                        miniatureEntities = emptyList()
                    )
                )
            }
        }


    override suspend fun addProject(project: ProjectBo): Long {
        val projectEntityData = projectDatabaseMapper.transformReverse(project)
        return projectDao.insertProject(projectEntityData.projectEntity)
    }

    override suspend fun updateProject(project: ProjectBo, avoidLastUpdate: Boolean): Boolean {
        val entity = projectDatabaseMapper.transformReverse(project).projectEntity
        return projectDao.updateProject(entity.copy(lastUpdate = if (avoidLastUpdate) 0 else clock.millis())) > 0

    }

    override suspend fun updateProjectProgress(projectId: Long, avoidLastUpdate: Boolean): Boolean =
        getProject(projectId).firstOrNull()?.let { project ->
            val newProgress: Float = if (project.minis.isNotEmpty()) {
                project.minis.map { it.percentage }.average().toFloat()
            } else {
                0f
            }
            updateProject(
                project = project.copy(progress = newProgress),
                avoidLastUpdate = avoidLastUpdate
            )
        } ?: false


    override suspend fun deleteProject(projectId: Long) {
        projectDao.deleteProject(projectId)

    }
}