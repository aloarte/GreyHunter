package com.devalr.minidetail

import app.cash.turbine.test
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import com.devalr.minidetail.interactions.Action.DeleteMiniature
import com.devalr.minidetail.interactions.Action.EditMiniature
import com.devalr.minidetail.interactions.Action.Load
import com.devalr.minidetail.interactions.Action.Return
import com.devalr.minidetail.interactions.Action.UpdateMilestone
import com.devalr.minidetail.interactions.ErrorType
import com.devalr.minidetail.interactions.Event.LaunchSnackBarError
import com.devalr.minidetail.interactions.Event.NavigateBack
import com.devalr.minidetail.interactions.Event.NavigateToEditMiniature
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
class MiniatureDetailViewModelTest {

    private val miniatureRepository: MiniatureRepository = mockk(relaxed = true)
    private val projectRepository: ProjectRepository = mockk(relaxed = true)
    private val tracer: AppTracer = mockk()

    private lateinit var viewModel: MiniatureDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    val miniId = 1L
    val projectId = 100L
    val miniature = MiniatureBo(id = miniId, projectId = projectId, name = "Space Marine")
    val project = ProjectBo(id = projectId, name = "Warhammer Army")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { tracer.log(any()) } just Runs
        viewModel = MiniatureDetailViewModel(tracer, miniatureRepository, projectRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN a valid projectId WHEN OnAppear is triggered THEN state updates with miniature and project`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(miniature)
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)

            // WHEN
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { miniatureRepository.getMiniature(miniId) }
            coVerify(exactly = 1) { projectRepository.getProject(projectId) }
            assertTrue(state.miniatureLoaded)
            assertEquals(miniature, state.miniature)
            assertEquals(project, state.parentProject)
        }

    @Test
    fun `GIVEN a wrong projectId WHEN OnAppear is triggered THEN error is raised`() = runTest {
        // GIVEN
        every { tracer.recordError(any()) } just Runs
        coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(miniature)
        coEvery { projectRepository.getProject(projectId) } returns flow {
            throw RuntimeException("Database error")
        }

        viewModel.events.test {
            // WHEN
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // THEN
            assertEquals(
                LaunchSnackBarError(ErrorType.RetrievingDatabase),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN a wrong miniId WHEN OnAppear is triggered THEN error is raised`() = runTest {
        // GIVEN
        every { tracer.recordError(any()) } just Runs
        coEvery { miniatureRepository.getMiniature(miniId) } returns flow {
            throw RuntimeException("Database error")
        }

        viewModel.events.test {
            // WHEN
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 0) { projectRepository.getProject(any()) }
            assertEquals(
                LaunchSnackBarError(ErrorType.RetrievingDatabase),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN a loaded miniature AND an achievable milestone WHEN OnMilestone is triggered THEN repository is updated and state reflects changes`() =
        runTest {
            // GIVEN
            val initialCompletion = MiniCompletionBo(isAssembled = false)
            coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(
                miniature.copy(completion = initialCompletion)
            )
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)

            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(UpdateMilestone(MilestoneType.Assembled, enable = true))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertTrue(state.miniature!!.completion.isAssembled)
            coVerify(exactly = 1) { miniatureRepository.updateMiniature(any()) }
            coVerify(exactly = 1) { projectRepository.updateProjectProgress(projectId) }
        }

    @Test
    fun `GIVEN a loaded miniature AND an unachievable milestone WHEN OnMilestone is triggered THEN error state is set AND repository is NOT called`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            val initialCompletion = MiniCompletionBo(isAssembled = false, isPrimed = false)

            coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(
                miniature.copy(completion = initialCompletion)
            )
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)

            viewModel.events.test {
                // GIVEN
                viewModel.onAction(Load(miniId))
                advanceUntilIdle()

                // WHEN
                viewModel.onAction(UpdateMilestone(MilestoneType.Primed, enable = true))
                advanceUntilIdle()

                // THEN
                assertEquals(
                    LaunchSnackBarError(ErrorType.CompletePreviousSteps),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            // THEN
            coVerify(exactly = 0) { miniatureRepository.updateMiniature(any()) }
            coVerify(exactly = 0) { projectRepository.updateProjectProgress(any()) }
        }

    @Test
    fun `GIVEN a not loaded miniature WHEN OnMilestone is triggered THEN error state`() = runTest {
        // GIVEN
        every { tracer.recordError(any()) } just Runs

        viewModel.events.test {
            // WHEN
            viewModel.onAction(UpdateMilestone(MilestoneType.Primed, enable = true))
            advanceUntilIdle()

            // THEN
            assertEquals(
                LaunchSnackBarError(ErrorType.EmptyMiniature),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Return is triggered THEN NavigateBack event is raised`() = runTest {
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
    fun `WHEN OnNavigateToEditMiniature is triggered THEN NavigateToEditMiniature event is raised`() =
        runTest {
            // GIVEN / THEN
            viewModel.events.test {
                // WHEN
                viewModel.onAction(
                    EditMiniature(
                        miniatureId = miniId,
                        projectId = projectId
                    )
                )

                // THEN
                assertEquals(
                    NavigateToEditMiniature(miniatureId = miniId, projectId = projectId),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN existent project WHEN OnDeleteProject is triggered THEN project is removed and NavigateBack event is raised`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.deleteMiniature(miniId) } returns true

            viewModel.events.test {
                // WHEN
                viewModel.onAction(DeleteMiniature(miniatureId = miniId))
                advanceUntilIdle()

                // THEN
                coVerify { miniatureRepository.deleteMiniature(miniId) }
                assertEquals(NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN not existent project WHEN OnDeleteProject is triggered THEN project isn't removed and no event is raised`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            coEvery { miniatureRepository.deleteMiniature(miniId) } returns false

            viewModel.events.test {
                // WHEN
                viewModel.onAction(DeleteMiniature(miniatureId = miniId))
                advanceUntilIdle()

                // THEN
                assertEquals(
                    LaunchSnackBarError(ErrorType.Delete),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            coVerify { miniatureRepository.deleteMiniature(miniId) }
        }
}