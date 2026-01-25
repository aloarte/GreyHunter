package com.devalr.settings

import android.net.Uri
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ProgressColorType.TrafficLight
import com.devalr.domain.enum.ThemeType.Dark
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.snackbar.SnackBarType.ERROR
import com.devalr.framework.components.snackbar.SnackBarType.SUCCESS
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnBackPressed
import com.devalr.settings.interactions.Action.OnChangeAppearance
import com.devalr.settings.interactions.Action.OnChangeProgressColors
import com.devalr.settings.interactions.Action.OnImportPressed
import com.devalr.settings.interactions.Event
import com.devalr.settings.interactions.Event.LaunchSnackBar
import com.devalr.settings.interactions.Event.NavigateBack
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository: SettingsRepository = mockk()
    private val uri: Uri = mockk()
    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    val projectId = 100L
    val project = ProjectBo(id = projectId, name = "Warhammer Army")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN loaded settings WHEN OnAppear is triggered THEN state updates with current status`() =
        runTest {
            // GIVEN
            val appVersion = "1.0"
            coEvery { repository.getAppearanceConfiguration() } returns flowOf(Dark)
            coEvery { repository.getProgressColorConfiguration() } returns flowOf(TrafficLight)
            coEvery { repository.getAppVersion() } returns flowOf(appVersion)
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { repository.getAppearanceConfiguration() }
            coVerify(exactly = 1) { repository.getProgressColorConfiguration() }
            coVerify(exactly = 1) { repository.getAppVersion() }
            assertTrue(state.settingsLoaded)
            assertEquals(Dark, state.themeType)
            assertEquals(TrafficLight, state.progressColorConfigType)
            assertEquals(appVersion, state.appVersion)
            assertNull(state.errorType)
        }


    @Test
    fun `WHEN OnBackPressed is triggered THEN NavigateBack event is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnBackPressed)
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateBack, events.first())
            job.cancel()
        }

    @Test
    fun `WHEN OnChangeAppearance is triggered THEN preference is updated`() =
        runTest {
            // GIVEN
            coEvery { repository.setAppearanceConfiguration(Dark) } just Runs

            // WHEN
            viewModel.onAction(OnChangeAppearance(Dark))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.setAppearanceConfiguration(Dark) }
        }

    @Test
    fun `WHEN OnChangeProgressColors is triggered THEN preference is updated`() =
        runTest {
            // GIVEN
            coEvery { repository.setProgressColorConfiguration(TrafficLight) } just Runs

            // WHEN
            viewModel.onAction(OnChangeProgressColors(TrafficLight))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.setProgressColorConfiguration(TrafficLight) }
        }


    @Test
    fun `WHEN OnImportPressed is triggered THEN repository importing is called with a success result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            coEvery { repository.importData(uri) } returns true

            // WHEN
            viewModel.onAction(OnImportPressed(uri))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.importData(uri) }
            assertEquals(1, events.size)
            assertEquals(LaunchSnackBar(true, SUCCESS), events.first())
            job.cancel()
        }

    @Test
    fun `WHEN OnImportPressed is triggered THEN repository importing is called with an error result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            coEvery { repository.importData(uri) } returns false

            // WHEN
            viewModel.onAction(OnImportPressed(uri))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.importData(uri) }
            assertEquals(1, events.size)
            assertEquals(LaunchSnackBar(true, ERROR), events.first())
            job.cancel()
        }


    @Test
    fun `WHEN OnExportPressed is triggered THEN repository exporting is called with a success result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            coEvery { repository.exportData(uri) } returns true

            // WHEN
            viewModel.onAction(Action.OnExportPressed(uri))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.exportData(uri) }
            assertEquals(1, events.size)
            assertEquals(LaunchSnackBar(false, SUCCESS), events.first())
            job.cancel()
        }


    @Test
    fun `WHEN OnExportPressed is triggered THEN repository exporting is called with an error result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            coEvery { repository.exportData(uri) } returns false

            // WHEN
            viewModel.onAction(Action.OnExportPressed(uri))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.exportData(uri) }
            assertEquals(1, events.size)
            assertEquals(LaunchSnackBar(false, ERROR), events.first())
            job.cancel()
        }
}