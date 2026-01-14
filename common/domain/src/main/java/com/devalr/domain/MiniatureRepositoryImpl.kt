package com.devalr.domain

import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock

class MiniatureRepositoryImpl(
    private val miniatureDao: MiniatureDao,
    private val miniatureDatabaseMapper: Mapper<MiniatureEntity, MiniatureBo>,
    private val clock: Clock = Clock.systemDefaultZone()
) : MiniatureRepository {
    override suspend fun addMiniature(miniature: MiniatureBo): Long {
        val entityMiniature = miniatureDatabaseMapper.transformReverse(miniature)
        return miniatureDao.insertMiniature(entityMiniature.copy(id = 0))
    }

    override suspend fun getMiniature(miniatureId: Long): Flow<MiniatureBo?> =
        miniatureDao.getMiniatureById(miniatureId).map { entity ->
            miniatureDatabaseMapper.transform(entity)
        }

    override suspend fun getLastUpdatedMiniatures(miniatureNumber: Int): Flow<List<MiniatureBo>> =
        miniatureDao.getLastUpdatedMiniatures(miniatureNumber).map { entityList ->
            entityList.map { entity ->
                miniatureDatabaseMapper.transform(entity)
            }
        }

    override suspend fun getMiniatures(miniaturesId: List<Long>): Flow<List<MiniatureBo>> =
        miniatureDao.getMiniaturesByIds(miniaturesId).map { entityList ->
            entityList.map { entity ->
                miniatureDatabaseMapper.transform(entity)
            }
        }

    override suspend fun getMiniaturesFromProject(projectId: Long): Flow<List<MiniatureBo>> =
        miniatureDao.getMiniaturesByProject(projectId)
            .map { entityList ->
                entityList.map { entity ->
                    miniatureDatabaseMapper.transform(entity)
                }
            }

    override suspend fun updateMiniature(miniature: MiniatureBo): Boolean {
        val entity = miniatureDatabaseMapper.transformReverse(miniature)
        val rowsAffected =
            miniatureDao.updateMiniature(entity.copy(lastUpdate = clock.millis()))
        return rowsAffected > 0
    }

    override suspend fun deleteMiniature(miniatureId: Long): Boolean =
        miniatureDao.deleteMiniature(miniatureId) > 0

}