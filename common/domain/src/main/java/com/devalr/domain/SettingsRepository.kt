package com.devalr.domain

import com.devalr.domain.enums.ProgressColorType
import com.devalr.domain.enums.ThemeType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAppearanceConfiguration(): Flow<ThemeType>

    suspend fun setAppearanceConfiguration(type: ThemeType)

    suspend fun getProgressColorConfiguration(): Flow<ProgressColorType>

    suspend fun setProgressColorConfiguration(colorType: ProgressColorType)

    suspend fun getAppVersion(): Flow<String>

    suspend fun setAppVersion(appVersion: String)

    suspend fun exportData(filePath: String) : Boolean

    suspend fun importData(filePath: String) : Boolean

}