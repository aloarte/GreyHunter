package com.devalr.painting


import app.cash.turbine.test
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.AppTracer
import com.devalr.painting.interactions.Action.FinishPainting
import com.devalr.painting.interactions.Action.Load
import com.devalr.painting.interactions.Action.Return
import com.devalr.painting.interactions.ErrorType.RetrievingDatabase
import com.devalr.painting.interactions.Event.LaunchSnackBarError
import com.devalr.painting.interactions.Event.NavigateBack
import com.devalr.painting.interactions.Event.NavigateToUpdateMiniatures
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class PaintingViewModelTest {

    private val miniatureRepository: MiniatureRepository = mockk()
    private val tracer: AppTracer = mockk()
    private lateinit var viewModel: PaintingViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val miniId1 = 1L
    private val miniId2 = 2L
    private val miniIds = listOf(miniId1, miniId2)

    private val miniature1 = MiniatureBo(id = miniId1, projectId = 1, name = "Space Marine")
    private val miniature2 =
        MiniatureBo(id = miniId2, projectId = 1, name = "Space Marine Intercessor")
    private val miniatures = listOf(miniature1, miniature2)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { tracer.log(any()) } just Runs
        viewModel = PaintingViewModel(tracer, miniatureRepository)
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

            // WHEN
            viewModel.onAction(Load(miniIds))
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
            every { tracer.recordError(any()) } just Runs
            coEvery { miniatureRepository.getMiniatures(miniIds) } returns flow {
                throw RuntimeException("Database error")
            }
            viewModel.events.test {
                // WHEN
                viewModel.onAction(Load(miniIds))
                advanceUntilIdle()

                // THEN
                assertEquals(LaunchSnackBarError(RetrievingDatabase), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { miniatureRepository.getMiniatures(miniIds) }
        }

    @Test
    fun `WHEN Return is triggered THEN NavigateBack event is raised`() = runTest {
        viewModel.events.test {
            // WHEN
            viewModel.onAction(Return)

            // THEN
            assertEquals(NavigateBack, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnDonePainting is triggered THEN NavigateToUpdateMiniatures event is raised`() =
        runTest {
            viewModel.events.test {
                // WHEN
                viewModel.onAction(FinishPainting(miniatureIds = miniIds))

                // THEN
                assertEquals(
                    NavigateToUpdateMiniatures(miniatureIds = miniIds),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
}