package com.devalr.domain

import android.net.Uri
import com.devalr.domain.enum.ThemeType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAppearanceConfiguration(): Flow<ThemeType>

    suspend fun setAppearanceConfiguration(type: ThemeType)

    suspend fun getAppVersion(): Flow<String>

    suspend fun setAppVersion(appVersion: String)

    suspend fun exportData(uri: Uri) : Boolean

    suspend fun importData(uri: Uri) : Boolean

}