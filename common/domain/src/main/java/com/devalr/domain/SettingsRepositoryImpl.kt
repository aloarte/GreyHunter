package com.devalr.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devalr.domain.enum.ThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val APPEARANCE_KEY = stringPreferencesKey("appearance_type")
    }

    override suspend fun getAppearanceConfiguration(): Flow<ThemeType> {
        return dataStore.data.map { preferences ->
            val name = preferences[APPEARANCE_KEY] ?: ThemeType.System.name
            ThemeType.valueOf(name)
        }
    }

    override suspend fun setAppearanceConfiguration(type: ThemeType) {
        dataStore.edit { preferences ->
            preferences[APPEARANCE_KEY] = type.name
        }
    }
}