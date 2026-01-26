package com.devalr.minidetail

import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.minidetail.interactions.Action
import com.devalr.minidetail.interactions.Action.Load
import com.devalr.minidetail.interactions.Action.Return
import com.devalr.minidetail.interactions.Action.DeleteMiniature
import com.devalr.minidetail.interactions.Action.UpdateMilestone
import com.devalr.minidetail.interactions.ErrorType
import com.devalr.minidetail.interactions.Event
import com.devalr.minidetail.interactions.Event.NavigateBack
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MiniatureDetailViewModelTest {

    private val miniatureRepository: MiniatureRepository = mockk(relaxed = true)
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    private lateinit var viewModel: MiniatureDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    val miniId = 1L
    val projectId = 100L
    val miniature = MiniatureBo(id = miniId, projectId = projectId, name = "Space Marine")
    val project = ProjectBo(id = projectId, name = "Warhammer Army")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MiniatureDetailViewModel(miniatureRepository, projectRepository)
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
            advanceUntilIdle()

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
            assertNull(state.error)
        }

    @Test
    fun `GIVEN a wrong projectId WHEN OnAppear is triggered THEN error is raised`() =
        runTest {
            // GIVEN:
            coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(miniature)
            coEvery { projectRepository.getProject(projectId) } returns flow {
                throw RuntimeException("Database error")
            }
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value

            coVerify(exactly = 1) { miniatureRepository.getMiniature(miniId) }
            coVerify(exactly = 1) { projectRepository.getProject(projectId) }
            assertEquals(ErrorType.RetrievingDatabase, state.error)
        }

    @Test
    fun `GIVEN a wrong miniId WHEN OnAppear is triggered THEN error is raised`() =
        runTest {
            // GIVEN:
            coEvery { miniatureRepository.getMiniature(miniId) } returns flow {
                throw RuntimeException("Database error")
            }
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value

            coVerify(exactly = 1) { miniatureRepository.getMiniature(miniId) }
            coVerify(exactly = 0) { projectRepository.getProject(projectId) }
            assertEquals(ErrorType.RetrievingDatabase, state.error)
        }


    @Test
    fun `GIVEN a loaded miniature AND an achievable milestone WHEN OnMilestone is triggered THEN repository is updated and state reflects changes`() =
        runTest {
            // GIVEN:
            val initialCompletion = MiniCompletionBo(isAssembled = false)
            coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(
                miniature.copy(
                    completion = initialCompletion
                )
            )
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(UpdateMilestone(MilestoneType.Assembled, enable = true))
            advanceUntilIdle()

            // THEN
            val currentState = viewModel.uiState.value
            assertTrue(currentState.miniature!!.completion.isAssembled)
            coVerify(exactly = 1) { miniatureRepository.updateMiniature(any()) }
            coVerify(exactly = 1) { projectRepository.updateProjectProgress(projectId) }

        }

    @Test
    fun `GIVEN a loaded miniature AND an unachievable milestone WHEN OnMilestone is triggered THEN error state is set AND repository is NOT called`() =
        runTest {
            // GIVEN
            val initialCompletion = MiniCompletionBo(isAssembled = false, isPrimed = false)
            coEvery { miniatureRepository.getMiniature(miniId) } returns flowOf(
                miniature.copy(
                    completion = initialCompletion
                )
            )
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)
            viewModel.onAction(Load(miniId))
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(UpdateMilestone(MilestoneType.Primed, enable = true))
            advanceUntilIdle()

            // THEN
            val currentState = viewModel.uiState.value
            assertEquals(ErrorType.CompletePreviousSteps, currentState.error)
            coVerify(exactly = 0) { miniatureRepository.updateMiniature(any()) }
            coVerify(exactly = 0) { projectRepository.updateProjectProgress(any()) }
        }

    @Test
    fun `GIVEN a not loaded miniature WHEN OnMilestone is triggered THEN error state`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(UpdateMilestone(MilestoneType.Primed, enable = true))
            advanceUntilIdle()

            // THEN
            val currentState = viewModel.uiState.value
            assertEquals(ErrorType.EmptyMiniature, currentState.error)
        }

    @Test
    fun `WHEN Return is triggered THEN NavigateBack event is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(Return)
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateBack, events.first())
            job.cancel()
        }

    @Test
    fun `WHEN OnNavigateToEditMiniature is triggered THEN NavigateToEditMiniature event is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(
                Action.EditMiniature(
                    miniatureId = miniId,
                    projectId = projectId
                )
            )
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(
                Event.NavigateToEditMiniature(miniatureId = miniId, projectId = projectId),
                events.first()
            )
            job.cancel()
        }

    @Test
    fun `GIVEN existent project WHEN OnDeleteProject is triggered THEN project is removed and NavigateBack event is raised`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.deleteMiniature(miniId) } returns true
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(DeleteMiniature(miniatureId = miniId))
            advanceUntilIdle()

            // THEN
            coVerify { miniatureRepository.deleteMiniature(miniId) }
            assertEquals(1, events.size)
            assertEquals(NavigateBack, events.first())
            job.cancel()
        }

    @Test
    fun `GIVEN not existent project WHEN OnDeleteProject is triggered THEN project isn't removed and no event is raised`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.deleteMiniature(miniId) } returns false
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(DeleteMiniature(miniatureId = miniId))
            advanceUntilIdle()

            // THEN
            coVerify { miniatureRepository.deleteMiniature(miniId) }
            assertEquals(0, events.size)
            job.cancel()
        }
}