package com.devalr.projectdetail

import app.cash.turbine.test
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import com.devalr.projectdetail.interactions.Action.DeleteProject
import com.devalr.projectdetail.interactions.Action.EditProject
import com.devalr.projectdetail.interactions.Action.Load
import com.devalr.projectdetail.interactions.Action.Return
import com.devalr.projectdetail.interactions.Action.SortMiniature
import com.devalr.projectdetail.interactions.ErrorType.Delete
import com.devalr.projectdetail.interactions.ErrorType.RetrievingDatabase
import com.devalr.projectdetail.interactions.Event.LaunchSnackBarError
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import com.devalr.projectdetail.model.SortDirection
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
class ProjectDetailViewModelTest {

    private val projectRepository: ProjectRepository = mockk()
    private val miniatureRepository: MiniatureRepository = mockk()
    private val tracer: AppTracer = mockk()
    private lateinit var viewModel: ProjectDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val projectId = 10L

    private val mini1 = MiniatureBo(
        id = 1L,
        projectId = projectId,
        name = "Technomancer",
        sortOrder = 1
    )

    private val mini2 = MiniatureBo(
        id = 2L,
        projectId = projectId,
        name = "Technomancer",
        sortOrder = 2
    )

    private val project = ProjectBo(
        id = projectId,
        name = "Hierotek Circle",
        progress = 0.5f,
        imageUri = null,
        minis = listOf(mini1, mini2)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { tracer.log(any()) } just Runs
        viewModel = ProjectDetailViewModel(tracer, projectRepository, miniatureRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN a project id WHEN OnAppear action is triggered THEN state updates with project data`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)

            // WHEN
            viewModel.onAction(Load(projectId))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertTrue(state.projectLoaded)
            assertEquals(project, state.project)
        }

    @Test
    fun `GIVEN a repository error WHEN OnAppear action is triggered THEN state updates with error message`() =
        runTest {
            // GIVEN
            val errorMessage = "Database connection failed"
            coEvery { projectRepository.getProject(projectId) } returns flow {
                throw Exception(errorMessage)
            }
            every { tracer.recordError(any()) } just Runs

            viewModel.events.test {
                // WHEN
                viewModel.onAction(Load(projectId))
                advanceUntilIdle()

                // THEN
                assertEquals(LaunchSnackBarError(RetrievingDatabase), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { projectRepository.getProject(projectId) }
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
    fun `WHEN OnNavigateToEditProject is triggered THEN NavigateToEditProject event is raised`() =
        runTest {
            // GIVEN / THEN
            viewModel.events.test {
                // WHEN
                viewModel.onAction(EditProject(projectId = projectId))

                // THEN
                assertEquals(NavigateToEditProject(projectId = projectId), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN existent project WHEN OnDeleteProject is triggered THEN project is removed and NavigateBack event is raised`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.deleteProject(projectId) } returns true

            viewModel.events.test {
                // WHEN
                viewModel.onAction(DeleteProject(projectId = projectId))

                // THEN
                assertEquals(NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify { projectRepository.deleteProject(projectId) }
        }

    @Test
    fun `GIVEN not existent project WHEN OnDeleteProject is triggered THEN project isn't removed and error event is raised`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.deleteProject(projectId) } returns false
            every { tracer.recordError(any()) } just Runs

            viewModel.events.test {
                // WHEN
                viewModel.onAction(DeleteProject(projectId = projectId))
                advanceUntilIdle()

                // THEN
                assertEquals(LaunchSnackBarError(Delete), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify { projectRepository.deleteProject(projectId) }
        }

    @Test
    fun `GIVEN sorted minis in project WHEN SortMiniature is triggered THEN sort effect is applied`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.getProject(projectId) } returns flowOf(project)
            coEvery { miniatureRepository.updateMiniature(mini1.copy(sortOrder = mini2.sortOrder)) } returns true
            coEvery { miniatureRepository.updateMiniature(mini2.copy(sortOrder = mini1.sortOrder)) } returns true
            viewModel.onAction(Load(projectId))
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(SortMiniature(order = SortDirection.Down, mini1.id))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(mini1.id to mini2.id, state.animatedMiniIds)
            coVerify(exactly = 2) { miniatureRepository.updateMiniature(any()) }

        }
}