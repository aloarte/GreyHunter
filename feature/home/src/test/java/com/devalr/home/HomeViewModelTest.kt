package com.devalr.home


import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.home.interactions.Action.OnAddProject
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.NavigateStartPaint
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProject
import com.devalr.home.model.ProjectVo.ProjectItem
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
class HomeViewModelTest {

    private val projectRepository: ProjectRepository = mockk()
    private val miniatureRepository: MiniatureRepository = mockk()

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val projects = listOf(
        ProjectBo(
            id = 1,
            name = "Project 1",
            progress = 0.5f,
            imageUri = null,
            minis = emptyList()
        ),
        ProjectBo(
            id = 2,
            name = "Project 2",
            progress = 1.0f,
            imageUri = null,
            minis = emptyList()
        )
    )
    private val miniature = MiniatureBo(
        id = 1,
        projectId = 1,
        name = "Miniature 1",
        imageUri = null
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(projectRepository, miniatureRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN projects exist WHEN OnAppear is triggered THEN state updates with project list and last updated items`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            coEvery { projectRepository.getLastUpdatedProject() } returns flowOf(projects[1])
            coEvery { miniatureRepository.getLastUpdatedMiniature() } returns flowOf(miniature)

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 1) { projectRepository.getLastUpdatedProject() }
            coVerify(exactly = 1) { miniatureRepository.getLastUpdatedMiniature() }
            val expectedProjects: List<ProjectVo> = listOf(
                ProjectItem(projects[0]),
                ProjectItem(projects[1]),
                AddProject
            )
            val state = viewModel.uiState.value
            assertTrue(state.loaded)
            assertEquals(expectedProjects, state.projects)
            assertEquals(miniature, state.lastUpdatedMini)
            assertEquals(projects[1], state.lastUpdatedProject)
            assertNull(state.error)
        }

    @Test
    fun `GIVEN projects exist WHEN OnAppear is triggered THEN state updates with project list and null last updated items`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            coEvery { projectRepository.getLastUpdatedProject() } returns flowOf(null)
            coEvery { miniatureRepository.getLastUpdatedMiniature() } returns flowOf(null)

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 1) { projectRepository.getLastUpdatedProject() }
            coVerify(exactly = 1) { miniatureRepository.getLastUpdatedMiniature() }
            val expectedProjects: List<ProjectVo> = listOf(
                ProjectItem(projects[0]),
                ProjectItem(projects[1]),
                AddProject
            )
            val state = viewModel.uiState.value
            assertTrue(state.loaded)
            assertEquals(expectedProjects, state.projects)
            assertNull(state.lastUpdatedMini)
            assertNull(state.lastUpdatedProject)
            assertNull(state.error)
        }


    @Test
    fun `GIVEN repository error WHEN OnAppear is triggered THEN state updates with error message`() =
        runTest {
            // GIVEN
            val errorMessage = "Error loading from database"
            coEvery { projectRepository.getAllProjects() } returns flow {
                throw Exception(errorMessage)
            }
            coEvery { projectRepository.getLastUpdatedProject() } returns flow {
                throw Exception(errorMessage)
            }
            coEvery { miniatureRepository.getLastUpdatedMiniature() } returns flow {
                throw Exception(errorMessage)
            }

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(errorMessage, state.error)
        }

    @Test
    fun `GIVEN user clicks project detail WHEN OnOpenProjectDetail is triggered THEN NavigateToProject event is sent`() =
        runTest {
            // GIVEN
            val projectId = 5L
            val events = mutableListOf<Event>()

            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnOpenProjectDetail(projectId))
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertTrue(events.first() is NavigateToProject)
            assertEquals(projectId, (events.first() as NavigateToProject).projectId)
            job.cancel()
        }

    @Test
    fun `GIVEN user clicks start painting WHEN OnStartPainting is triggered THEN LaunchStartPaintModal event is sent`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnStartPainting)
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateStartPaint, events.first())
            job.cancel()
        }

    @Test
    fun `GIVEN user clicks add project WHEN OnAddProject is triggered THEN NavigateToAddProject event is sent`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnAddProject)
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateToAddProject, events.first())
            job.cancel()
        }

}