package com.devalr.domain

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devalr.domain.enum.ThemeType
import com.devalr.domain.file.CSVManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val projectRepository: ProjectRepository,
    private val csvManager: CSVManager,
    private val context: Context
) : SettingsRepository {

    private companion object {
        val APPEARANCE_KEY = stringPreferencesKey("appearance_type")
        val APP_VERSION_KEY = stringPreferencesKey("app_version")

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

    override suspend fun getAppVersion(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[APP_VERSION_KEY] ?: "0.0"
        }
    }

    override suspend fun setAppVersion(appVersion: String) {
        dataStore.edit { preferences ->
            preferences[APP_VERSION_KEY] = appVersion
        }
    }

    override suspend fun exportData(uri: Uri): Boolean = try {
        val projects = projectRepository.getAllProjects().first()
        context.contentResolver.openOutputStream(uri)?.let { outputStream ->
            csvManager.writeProjectsToCSV(outputStream, projects)
        } ?: false
    } catch (e: Exception) {
        //TODO Error, output stream wasn't opened
        false
    }


    override suspend fun importData(uri: Uri): Boolean = try {
        context.contentResolver.openInputStream(uri)?.let { inputStream ->
            val projectsWithMinis = csvManager.readProjectsFromCSV(inputStream)
            projectRepository.addAllProjects(projects = projectsWithMinis, resetDatabase = true)
        } ?: false

    } catch (e: Exception) {
        //TODO Error, input stream wasn't opened
        false
    }

}