package com.devalr.domain

import android.content.ContentResolver
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ThemeType
import com.devalr.domain.file.CSVManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val projectRepository: ProjectRepository,
    private val csvManager: CSVManager,
    private val contentResolver: ContentResolver
) : SettingsRepository {

    private companion object {
        val APPEARANCE_KEY = stringPreferencesKey("appearance_type")
        val PROGRESS_COLOR_KEY = stringPreferencesKey("progress_color")
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

    override suspend fun getProgressColorConfiguration(): Flow<ProgressColorType> {

        return dataStore.data.map { preferences ->
            val name = preferences[PROGRESS_COLOR_KEY] ?: ProgressColorType.Brand.name
            ProgressColorType.valueOf(name)
        }
    }

    override suspend fun setProgressColorConfiguration(colorType: ProgressColorType) {
        dataStore.edit { preferences ->
            preferences[PROGRESS_COLOR_KEY] = colorType.name
        }
    }

    override suspend fun getAppVersion(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[APP_VERSION_KEY] ?: ""
        }
    }

    override suspend fun setAppVersion(appVersion: String) {
        dataStore.edit { preferences ->
            preferences[APP_VERSION_KEY] = appVersion
        }
    }

    override suspend fun exportData(uri: Uri): Boolean = try {
        val projects = projectRepository.getAllProjects().first()
        contentResolver.openOutputStream(uri)?.let { outputStream ->
            csvManager.writeProjectsToCSV(outputStream, projects)
        } ?: false
    } catch (e: Exception) {
        //TODO Error, output stream wasn't opened
        false
    }


    override suspend fun importData(uri: Uri): Boolean = try {
        contentResolver.openInputStream(uri)?.let { inputStream ->
            val projectsWithMinis = csvManager.readProjectsFromCSV(inputStream)
            if (projectsWithMinis.isNotEmpty()) {
                projectRepository.addAllProjects(projects = projectsWithMinis, resetDatabase = true)
            } else false
        } ?: false

    } catch (e: Exception) {
        //TODO Error, input stream wasn't opened
        false
    }

}