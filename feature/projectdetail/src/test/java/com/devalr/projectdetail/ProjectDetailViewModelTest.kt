package com.devalr.projectdetail

import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.ProjectBo
import com.devalr.projectdetail.interactions.Action.Load
import com.devalr.projectdetail.interactions.Action.Return
import com.devalr.projectdetail.interactions.Action.DeleteProject
import com.devalr.projectdetail.interactions.Action.EditProject
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
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
class ProjectDetailViewModelTest {

    private val repository: ProjectRepository = mockk()
    private lateinit var viewModel: ProjectDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val projectId = 10L
    private val project = ProjectBo(
        id = projectId,
        name = "Hierotek Circle",
        progress = 0.5f,
        imageUri = null,
        minis = emptyList()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProjectDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN a project id WHEN OnAppear action is triggered THEN state updates with project data`() =
        runTest {
            // GIVEN
            coEvery { repository.getProject(projectId) } returns flowOf(project)

            // WHEN
            viewModel.onAction(Load(projectId))
            advanceUntilIdle()

            // THEN
            val currentState = viewModel.uiState.value
            assertTrue(currentState.projectLoaded)
            assertEquals(project, currentState.project)
            assertNull(currentState.error)
        }

    @Test
    fun `GIVEN a repository error WHEN OnAppear action is triggered THEN state updates with error message`() =
        runTest {
            // GIVEN
            val errorMessage = "Database connection failed"
            coEvery { repository.getProject(projectId) } returns flow {
                throw Exception(errorMessage)
            }

            // WHEN
            viewModel.onAction(Load(projectId))
            advanceUntilIdle()

            // THEN
            val currentState = viewModel.uiState.value
            assertEquals(errorMessage, currentState.error)
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
    fun `WHEN OnNavigateToEditProject is triggered THEN NavigateToEditProject event is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(EditProject(projectId = projectId))
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateToEditProject(projectId = projectId), events.first())
            job.cancel()
        }

    @Test
    fun `GIVEN existent project WHEN OnDeleteProject is triggered THEN project is removed and NavigateBack event is raised`() =
        runTest {
            // GIVEN
            coEvery { repository.deleteProject(projectId) } returns true
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(DeleteProject(projectId = projectId))
            advanceUntilIdle()

            // THEN
            coVerify { repository.deleteProject(projectId) }
            assertEquals(1, events.size)
            assertEquals(NavigateBack, events.first())
            job.cancel()
        }

    @Test
    fun `GIVEN not existent project WHEN OnDeleteProject is triggered THEN project isn't removed and no event is raised`() =
        runTest {
            // GIVEN
            coEvery { repository.deleteProject(projectId) } returns false
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(DeleteProject(projectId = projectId))
            advanceUntilIdle()

            // THEN
            coVerify { repository.deleteProject(projectId) }
            assertEquals(0, events.size)
            job.cancel()
        }
}