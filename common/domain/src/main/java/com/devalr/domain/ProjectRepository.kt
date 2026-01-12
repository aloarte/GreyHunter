package com.devalr.domain

import com.devalr.domain.model.ProjectBo
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {

    suspend fun getAllProjects(): Flow<List<ProjectBo>>

    suspend fun getProject(projectId: Long): Flow<ProjectBo?>

    suspend fun getLastUpdatedProject(): Flow<ProjectBo?>

    suspend fun addProject(project: ProjectBo): Long

    suspend fun updateProject(project: ProjectBo, avoidLastUpdate: Boolean = false): Boolean

    suspend fun updateProjectProgress(projectId: Long, avoidLastUpdate: Boolean = false): Boolean

    suspend fun deleteProject(projectId: Long)

}