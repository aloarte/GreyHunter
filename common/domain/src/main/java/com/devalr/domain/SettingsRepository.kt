package com.devalr.domain

import com.devalr.domain.enum.DarkModeType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getDarkModeConfiguration(): Flow<DarkModeType>

    suspend fun setDarkModeConfiguration(type: DarkModeType)
}