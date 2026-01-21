package com.devalr.settings

import com.devalr.domain.SettingsRepository
import com.devalr.domain.enum.ThemeType.Dark
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Action.OnBackPressed
import com.devalr.settings.interactions.Action.OnChangeAppearance
import com.devalr.settings.interactions.Event
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
class MiniatureDetailViewModelTest {

    private val repository: SettingsRepository = mockk()

    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    val miniId = 1L
    val projectId = 100L
    val miniature = MiniatureBo(id = miniId, projectId = projectId, name = "Space Marine")
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
            coEvery { repository.getAppearanceConfiguration() } returns flowOf(Dark)
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { repository.getAppearanceConfiguration() }
            assertTrue(state.settingsLoaded)
            assertEquals(Dark, state.themeType)
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
}