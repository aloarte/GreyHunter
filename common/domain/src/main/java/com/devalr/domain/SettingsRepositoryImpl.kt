package com.devalr.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devalr.domain.enum.AppearanceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val APPEARANCE_KEY = stringPreferencesKey("appearance_type")
    }

    override suspend fun getAppearanceConfiguration(): Flow<AppearanceType> {
        return dataStore.data.map { preferences ->
            val name = preferences[APPEARANCE_KEY] ?: AppearanceType.System.name
            AppearanceType.valueOf(name)
        }
    }

    override suspend fun setAppearanceConfiguration(type: AppearanceType) {
        dataStore.edit { preferences ->
            preferences[APPEARANCE_KEY] = type.name
        }
    }
}