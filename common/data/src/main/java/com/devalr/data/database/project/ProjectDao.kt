package com.devalr.data.database.project


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity): Int

    @Query("DELETE FROM projects WHERE id = :projectId")
    suspend fun deleteProject(projectId: Long): Int

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects(): Int

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<ProjectEntity>

    @Query("SELECT * FROM projects WHERE lastUpdate > 0 ORDER BY lastUpdate DESC LIMIT 1")
    fun getLastUpdatedProject(): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE completionPercentage > 0.8 AND completionPercentage < 1 ORDER BY completionPercentage DESC LIMIT :projectNumber")
    fun getAlmostDoneProjects(projectNumber: Int): Flow<List<ProjectEntity>>
}