package com.devalr.domain

import com.devalr.domain.enum.AppearanceType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAppearanceConfiguration(): Flow<AppearanceType>

    suspend fun setAppearanceConfiguration(type: AppearanceType)
}