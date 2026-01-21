package com.devalr.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.devalr.domain.SettingsRepositoryImpl
import com.devalr.domain.enum.ThemeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        val file = File(temporaryFolder.root, "test.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO)
        ) { file }

        repository = SettingsRepositoryImpl(dataStore)
    }

    @Test
    fun `GIVEN empty datastore WHEN getAppearanceConfiguration is called THEN default value is returned`() =
        runBlocking {
            // GIVEN & WHEN
            val result = repository.getAppearanceConfiguration().first()

            // THEN
            assertEquals(ThemeType.System, result)
        }

    @Test
    fun `GIVEN not empty datastore WHEN getAppearanceConfiguration is called THEN value is returned`() =
        runBlocking {
            // GIVEN
            repository.setAppearanceConfiguration(ThemeType.Dark)

            // WHEN
            val result = repository.getAppearanceConfiguration().first()

            // THEN
            assertEquals(ThemeType.Dark, result)
        }
}