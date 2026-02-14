package com.devalr.settings

import android.net.Uri
import app.cash.turbine.test
import com.devalr.domain.SettingsRepository
import com.devalr.domain.enums.ProgressColorType.TrafficLight
import com.devalr.domain.enums.ThemeType.Dark
import com.devalr.framework.AppTracer
import com.devalr.framework.components.snackbar.SnackBarType.ERROR
import com.devalr.framework.components.snackbar.SnackBarType.SUCCESS
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.ChangeAppearance
import com.devalr.settings.interactions.Action.ChangeProgressColors
import com.devalr.settings.interactions.Action.ImportProjects
import com.devalr.settings.interactions.Action.Load
import com.devalr.settings.interactions.Action.Return
import com.devalr.settings.interactions.ErrorType
import com.devalr.settings.interactions.Event.LaunchSnackBar
import com.devalr.settings.interactions.Event.NavigateBack
import com.devalr.settings.interactions.OperationType
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository: SettingsRepository = mockk()
    private val tracer: AppTracer = mockk()
    private val uri: Uri = mockk()
    private lateinit var viewModel: SettingsViewModel

    private val fakeUri = "content://fake-uri"

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { tracer.log(any()) } just Runs
        every { uri.toString() } returns fakeUri
        viewModel = SettingsViewModel(tracer, repository)
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

            // WHEN
            viewModel.onAction(Load)
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
        }

    @Test
    fun `WHEN Return is triggered THEN NavigateBack event is raised`() =
        runTest {
            // GIVEN / THEN
            viewModel.events.test {
                // WHEN
                viewModel.onAction(Return)

                // THEN
                assertEquals(NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnChangeAppearance is triggered THEN preference is updated`() =
        runTest {
            // GIVEN
            coEvery { repository.setAppearanceConfiguration(Dark) } just Runs

            // WHEN
            viewModel.onAction(ChangeAppearance(Dark))
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
            viewModel.onAction(ChangeProgressColors(TrafficLight))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { repository.setProgressColorConfiguration(TrafficLight) }
        }

    @Test
    fun `WHEN OnImport is triggered THEN repository importing is called with a success result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            coEvery { repository.importData(fakeUri) } returns true

            viewModel.events.test {
                // WHEN
                viewModel.onAction(ImportProjects(uri))

                // THEN
                assertEquals(LaunchSnackBar(SUCCESS, operation = OperationType.Import), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.importData(fakeUri) }
        }

    @Test
    fun `WHEN OnImportProjects is triggered THEN repository importing is called with an error result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            coEvery { repository.importData(fakeUri) } returns false

            viewModel.events.test {
                // WHEN
                viewModel.onAction(ImportProjects(uri))

                // THEN
                assertEquals(LaunchSnackBar(ERROR, ErrorType.Import), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.importData(fakeUri) }
        }

    @Test
    fun `WHEN ExportProjects is triggered THEN repository exporting is called with a success result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            coEvery { repository.exportData(fakeUri) } returns true

            viewModel.events.test {
                // WHEN
                viewModel.onAction(Action.ExportProjects(uri))

                // THEN
                assertEquals(LaunchSnackBar(SUCCESS, operation = OperationType.Export), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.exportData(fakeUri) }
        }

    @Test
    fun `WHEN ExportProjects is triggered THEN repository exporting is called with an error result and LaunchSnackBar event is triggered`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            coEvery { repository.exportData(fakeUri) } returns false

            viewModel.events.test {
                // WHEN
                viewModel.onAction(Action.ExportProjects(uri))

                // THEN
                assertEquals(LaunchSnackBar(ERROR, ErrorType.Export), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.exportData(fakeUri) }
        }
}