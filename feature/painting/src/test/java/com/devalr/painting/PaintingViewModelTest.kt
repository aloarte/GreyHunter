package com.devalr.painting


import com.devalr.domain.MiniatureRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.painting.interactions.Action.OnAppear
import com.devalr.painting.interactions.Action.OnBackPressed
import com.devalr.painting.interactions.Action.OnDonePainting
import com.devalr.painting.interactions.ErrorType
import com.devalr.painting.interactions.Event
import com.devalr.painting.interactions.Event.NavigateBack
import com.devalr.painting.interactions.Event.NavigateToUpdateMiniatures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaintingViewModelTest {

    private val miniatureRepository: MiniatureRepository = mockk()
    private lateinit var viewModel: PaintingViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val miniId1 = 1L
    private val miniId2 = 2L
    private val miniature1 = MiniatureBo(id = miniId1, projectId = 1, name = "Space Marine")
    private val miniature2 =
        MiniatureBo(id = miniId2, projectId = 1, name = "Space Marine Intercessor")
    private val miniIds = listOf(miniId1, miniId2)
    private val miniatures = listOf(miniature1, miniature2)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PaintingViewModel(miniatureRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `GIVEN a valid set of N miniIds WHEN OnAppear is triggered THEN state updates with the N miniatures`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.getMiniatures(miniIds) } returns flowOf(miniatures)
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAppear(miniIds))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { miniatureRepository.getMiniatures(miniIds) }
            assertTrue(state.minisLoaded)
            assertEquals(miniatures, state.miniatures)
        }

    @Test
    fun `GIVEN a wrong set of N miniIds WHEN OnAppear is triggered THEN error is raised`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.getMiniatures(miniIds) } returns flow {
                throw RuntimeException("Database error")
            }
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAppear(miniIds))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { miniatureRepository.getMiniatures(miniIds) }
            assertEquals(ErrorType.RetrievingDatabase, state.error)

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
    fun `WHEN OnDonePainting is triggered THEN NavigateToUpdateMiniatures event is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnDonePainting(miniatureIds = miniIds))
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(
                NavigateToUpdateMiniatures(miniatureIds = miniIds),
                events.first()
            )
            job.cancel()
        }
}