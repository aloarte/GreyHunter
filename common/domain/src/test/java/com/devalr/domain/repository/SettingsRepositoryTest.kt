package com.devalr.domain.repository

import android.content.ContentResolver
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.devalr.domain.ProjectRepository
import com.devalr.domain.SettingsRepositoryImpl
import com.devalr.domain.enum.ThemeType
import com.devalr.domain.file.CSVManager
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepositoryImpl
    private var csvManager: CSVManager = mockk()
    val contentResolver: ContentResolver = mockk()
    private var projectRepository: ProjectRepository = mockk()

    private val projects = listOf(hierotekCircleProject, stormlightArchiveProject)


    @Before
    fun setUp() {
        val file = File(temporaryFolder.root, "test.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO)
        ) { file }
        repository =
            SettingsRepositoryImpl(dataStore, projectRepository, csvManager, contentResolver)
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

    @Test
    fun `GIVEN empty datastore WHEN getAppVersion is called THEN default value is returned`() =
        runBlocking {
            // GIVEN & WHEN
            val result = repository.getAppVersion().first()

            // THEN
            assertEquals("", result)
        }

    @Test
    fun `GIVEN not empty datastore WHEN getAppVersion is called THEN value is returned`() =
        runBlocking {
            // GIVEN
            val appVersion = "1.1"
            repository.setAppVersion(appVersion)

            // WHEN
            val result = repository.getAppVersion().first()

            // THEN
            assertEquals(appVersion, result)
        }

    @Test
    fun `GIVEN projects WHEN exportData THEN csv file is created`() =
        runBlocking {
            // GIVEN
            coEvery { csvManager.writeProjectsToCSV(any(), projects) } returns true
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            every { contentResolver.openOutputStream(any()) } returns ByteArrayOutputStream()
            val outputFile = File(temporaryFolder.root, "export.csv")
            val uri = Uri.fromFile(outputFile)

            // WHEN
            val result = repository.exportData(uri)

            // THEN
            coVerify(exactly = 1) { csvManager.writeProjectsToCSV(any(), projects) }
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            assertTrue(result)
        }

    @Test
    fun `GIVEN projects WHEN exportData THEN csv manager fails`() =
        runBlocking {
            // GIVEN
            coEvery { csvManager.writeProjectsToCSV(any(), projects) } returns false
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            every { contentResolver.openOutputStream(any()) } returns ByteArrayOutputStream()
            val outputFile = File(temporaryFolder.root, "export.csv")
            val uri = Uri.fromFile(outputFile)

            // WHEN
            val result = repository.exportData(uri)

            // THEN
            coVerify(exactly = 1) { csvManager.writeProjectsToCSV(any(), any()) }
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            assertFalse(result)
        }

    @Test
    fun `GIVEN projects WHEN exportData THEN open stream fails`() =
        runBlocking {
            // GIVEN
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            every { contentResolver.openOutputStream(any()) } returns null
            val outputFile = File(temporaryFolder.root, "export.csv")
            val uri = Uri.fromFile(outputFile)

            // WHEN
            val result = repository.exportData(uri)

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 0) { csvManager.writeProjectsToCSV(any(), projects) }
            assertFalse(result)
        }

    @Test
    fun `GIVEN csv file WHEN importData THEN projects are inserted`() =
        runBlocking {
            // GIVEN
            every { contentResolver.openInputStream(any()) } returns ByteArrayInputStream("test".toByteArray())
            coEvery { csvManager.readProjectsFromCSV(any()) } returns projects
            coEvery { projectRepository.addAllProjects(projects, true) } returns true
            val inputFile = File(temporaryFolder.root, "import.csv")
            val uri = Uri.fromFile(inputFile)

            // WHEN
            val result = repository.importData(uri)

            // THEN
            assertTrue(result)
            coVerify(exactly = 1) { csvManager.readProjectsFromCSV(any()) }
            coVerify(exactly = 1) { projectRepository.addAllProjects(projects, true) }
        }

    @Test
    fun `GIVEN csv file WHEN importData THEN open stream fails`() =
        runBlocking {
            // GIVEN
            every { contentResolver.openInputStream(any()) } returns null
            val inputFile = File(temporaryFolder.root, "import.csv")
            val uri = Uri.fromFile(inputFile)

            // WHEN
            val result = repository.importData(uri)

            // THEN
            assertFalse(result)
            coVerify(exactly = 0) { csvManager.readProjectsFromCSV(any()) }
            coVerify(exactly = 0) { projectRepository.addAllProjects(projects, any()) }
        }

    @Test
    fun `GIVEN csv file WHEN importData THEN csv manager fails`() =
        runBlocking {
            // GIVEN
            coEvery { csvManager.readProjectsFromCSV(any()) } returns emptyList()
            every { contentResolver.openInputStream(any()) } returns ByteArrayInputStream("test".toByteArray())
            val inputFile = File(temporaryFolder.root, "import.csv")
            val uri = Uri.fromFile(inputFile)

            // WHEN
            val result = repository.importData(uri)

            // THEN
            assertFalse(result)
            coVerify(exactly = 1) { csvManager.readProjectsFromCSV(any()) }
            coVerify(exactly = 0) { projectRepository.addAllProjects(projects, any()) }
        }


}