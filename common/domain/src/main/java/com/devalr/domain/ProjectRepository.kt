package com.devalr.domain

import com.devalr.domain.model.ProjectBo
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {

    suspend fun getAllProjects(): Flow<List<ProjectBo>>

    suspend fun addProject(project: ProjectBo): Long

    suspend fun getProject(projectId: Long): Flow<ProjectBo>

    suspend fun updateProject(project: ProjectBo): Boolean

    suspend fun updateProjectProgress(projectId: Long): Boolean

    suspend fun deleteProject(projectId: Long)

}