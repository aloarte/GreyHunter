package com.devalr.domain

import com.devalr.domain.enum.DarkModeType
import com.devalr.domain.model.ProjectBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SettingsRepositoryImpl: SettingsRepository {
    override suspend fun getDarkModeConfiguration(): Flow<DarkModeType> {
        return flowOf(DarkModeType.Dark)
    }
}