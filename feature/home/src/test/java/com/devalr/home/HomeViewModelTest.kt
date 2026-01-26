package com.devalr.home


import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.home.interactions.Action.AddProject
import com.devalr.home.interactions.Action.Load
import com.devalr.home.interactions.Action.OpenProjectDetail
import com.devalr.home.interactions.Action.StartPainting
import com.devalr.home.interactions.Action.UpdateGamificationMessage
import com.devalr.home.interactions.Event
import com.devalr.home.interactions.Event.NavigateToStartPaint
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.model.GamificationMessageType.AlmostDone
import com.devalr.home.model.GamificationMessageType.EmptyProjects
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.GamificationMessageType.ProgressRange
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProjectItem
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

    private val project50 = ProjectBo(
        id = 1,
        name = "Project 1",
        progress = 0.5f,
        imageUri = null,
        minis = emptyList()
    )

    private val project100 = ProjectBo(
        id = 2,
        name = "Project 2",
        progress = 1.0f,
        imageUri = null,
        minis = emptyList()
    )
    private val projects = listOf(project50, project100)
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
            coEvery { projectRepository.getAlmostDoneProjects() } returns flowOf(listOf(projects[1]))
            coEvery { miniatureRepository.getLastUpdatedMiniatures() } returns flowOf(
                listOf(
                    miniature
                )
            )

            // WHEN
            viewModel.onAction(Load)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 1) { projectRepository.getAlmostDoneProjects() }
            coVerify(exactly = 1) { miniatureRepository.getLastUpdatedMiniatures() }
            val expectedProjects: List<ProjectVo> = listOf(
                ProjectItem(projects[0]),
                ProjectItem(projects[1]),
                AddProjectItem
            )
            val state = viewModel.uiState.value
            assertTrue(state.loaded)
            assertEquals(expectedProjects, state.projects)
            assertEquals(listOf(miniature), state.lastUpdatedMinis)
            assertEquals(listOf(projects[1]), state.almostDoneProjects)
            assertNull(state.error)
        }

    @Test
    fun `GIVEN projects exist WHEN OnAppear is triggered THEN state updates with project list and null last updated items`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            coEvery { projectRepository.getAlmostDoneProjects() } returns flowOf(emptyList())
            coEvery { miniatureRepository.getLastUpdatedMiniatures() } returns flowOf(emptyList())

            // WHEN
            viewModel.onAction(Load)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 1) { projectRepository.getAlmostDoneProjects() }
            coVerify(exactly = 1) { miniatureRepository.getLastUpdatedMiniatures() }
            val expectedProjects: List<ProjectVo> = listOf(
                ProjectItem(projects[0]),
                ProjectItem(projects[1]),
                AddProjectItem
            )
            val state = viewModel.uiState.value
            assertTrue(state.loaded)
            assertEquals(expectedProjects, state.projects)
            assertEquals(emptyList<MiniatureBo>(), state.lastUpdatedMinis)
            assertEquals(emptyList<ProjectBo>(), state.almostDoneProjects)
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
            coEvery { projectRepository.getAlmostDoneProjects() } returns flow {
                throw Exception(errorMessage)
            }
            coEvery { miniatureRepository.getLastUpdatedMiniatures() } returns flow {
                throw Exception(errorMessage)
            }

            // WHEN
            viewModel.onAction(Load)
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
            viewModel.onAction(OpenProjectDetail(projectId))
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
            viewModel.onAction(StartPainting)
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateToStartPaint, events.first())
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
            viewModel.onAction(AddProject)
            advanceUntilIdle()

            // THEN
            assertEquals(1, events.size)
            assertEquals(NavigateToAddProject, events.first())
            job.cancel()
        }

    @Test
    fun `GIVEN empty projects WHEN OnUploadGamificationMessage is triggered THEN no message is raised`() =
        runTest {
            // GIVEN
            val projects = emptyList<ProjectBo>()
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(None, state.gamificationSentence)
        }

    @Test
    fun `GIVEN not started projects WHEN OnUploadGamificationMessage is triggered THEN no message is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 0.0f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(EmptyProjects, state.gamificationSentence)
        }

    @Test
    fun `GIVEN 20 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 0.18f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.2f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 50 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 0.28f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.5f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 70 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 0.58f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.7f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 90 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 0.78f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.9f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 99 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 0.98f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.99f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 100 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = listOf(project50.copy(progress = 1.0f))
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 1.0f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN selected project WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            // GIVEN
            val projects = projects
            val almostDoneProjects = listOf(project50.copy(progress = 1.0f))

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects = projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(AlmostDone(projectName = project50.name), state.gamificationSentence)
        }
}