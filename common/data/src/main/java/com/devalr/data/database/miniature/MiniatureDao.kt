package com.devalr.data.database.miniature

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MiniatureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMiniature(miniature: MiniatureEntity): Long

    @Update
    suspend fun updateMiniature(miniature: MiniatureEntity) : Int

    @Query("DELETE FROM miniatures WHERE id = :miniatureId")
    suspend fun deleteMiniature(miniatureId: Long) : Int

    @Query("SELECT * FROM miniatures WHERE projectId = :projectId")
    fun getMiniaturesByProject(projectId: Long): Flow<List<MiniatureEntity>>

    @Query("SELECT * FROM miniatures WHERE id = :miniatureId")
    fun getMiniatureById(miniatureId: Long): Flow<MiniatureEntity>

    @Query("SELECT * FROM miniatures WHERE lastUpdate > 0 ORDER BY lastUpdate DESC LIMIT :miniatureNumber")
    fun getLastUpdatedMiniatures(miniatureNumber:Int): Flow<List<MiniatureEntity>>

    @Query("SELECT * FROM miniatures WHERE id IN (:miniatureIds)")
    fun getMiniaturesByIds(miniatureIds: List<Long>): Flow<List<MiniatureEntity>>
}