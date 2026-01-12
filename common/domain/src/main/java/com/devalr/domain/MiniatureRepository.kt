package com.devalr.domain

import com.devalr.domain.model.MiniatureBo
import kotlinx.coroutines.flow.Flow

interface MiniatureRepository {

    suspend fun addMiniature(miniature: MiniatureBo): Long

    suspend fun getMiniature(miniatureId: Long): Flow<MiniatureBo?>

    suspend fun getLastUpdatedMiniature(): Flow<MiniatureBo?>

    suspend fun getMiniatures(miniaturesId: List<Long>): Flow<List<MiniatureBo>>

    suspend fun getMiniaturesFromProject(projectId: Long): Flow<List<MiniatureBo>>

    suspend fun updateMiniature(miniature: MiniatureBo): Boolean

    suspend fun deleteMiniature(miniatureId: Long)

}