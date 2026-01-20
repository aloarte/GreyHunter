package com.devalr.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devalr.domain.enum.DarkModeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val DARK_MODE_KEY = stringPreferencesKey("dark_mode_type")
    }

    override suspend fun getDarkModeConfiguration(): Flow<DarkModeType> {
        return dataStore.data.map { preferences ->
            val name = preferences[DARK_MODE_KEY] ?: DarkModeType.System.name
            DarkModeType.valueOf(name)
        }
    }

    override suspend fun setDarkModeConfiguration(type: DarkModeType) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = type.name
        }
    }
}