package com.devalr.home


import app.cash.turbine.test
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import com.devalr.home.interactions.Action.AddProject
import com.devalr.home.interactions.Action.Load
import com.devalr.home.interactions.Action.OpenProjectDetail
import com.devalr.home.interactions.Action.StartPainting
import com.devalr.home.interactions.Action.UpdateGamificationMessage
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.Event.NavigateToStartPaint
import com.devalr.home.model.GamificationMessageType.AlmostDone
import com.devalr.home.model.GamificationMessageType.EmptyProjects
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.GamificationMessageType.ProgressRange
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProjectItem
import com.devalr.home.model.ProjectVo.ProjectItem
import com.devalr.home.model.ProjectsStats
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val projectRepository: ProjectRepository = mockk()
    private val miniatureRepository: MiniatureRepository = mockk()
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val tracer: AppTracer = mockk()

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
        every { tracer.log(any()) } just Runs
        viewModel = HomeViewModel(tracer, projectRepository, miniatureRepository)
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
            coEvery { projectRepository.getAlmostDoneProjects(3) } returns flowOf(listOf(projects[1]))
            coEvery { miniatureRepository.getLastUpdatedMiniatures(3) } returns flowOf(
                listOf(                    miniature)
            )

            // WHEN
            viewModel.onAction(Load(true))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 1) { projectRepository.getAlmostDoneProjects(3) }
            coVerify(exactly = 1) { miniatureRepository.getLastUpdatedMiniatures(3) }

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
            assertEquals(ProjectsStats(75,0,0,1,2), state.stats)
            assertFalse(state.error)
        }

    @Test
    fun `GIVEN projects exist WHEN OnAppear is triggered THEN state updates with project list and null last updated items`() =
        runTest {
            // GIVEN
            coEvery { projectRepository.getAllProjects() } returns flowOf(projects)
            coEvery { projectRepository.getAlmostDoneProjects(2) } returns flowOf(emptyList())
            coEvery { miniatureRepository.getLastUpdatedMiniatures(2) } returns flowOf(emptyList())

            // WHEN
            viewModel.onAction(Load(false))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectRepository.getAllProjects() }
            coVerify(exactly = 1) { projectRepository.getAlmostDoneProjects(2) }
            coVerify(exactly = 1) { miniatureRepository.getLastUpdatedMiniatures(2) }

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
            assertFalse(state.error)
        }

    @Test
    fun `GIVEN repository error WHEN OnAppear is triggered THEN state updates with error message`() =
        runTest {
            // GIVEN
            val errorMessage = "Error loading from database"
            every { tracer.recordError(any()) } just Runs
            coEvery { projectRepository.getAllProjects() } returns flow {
                throw Exception(errorMessage)
            }
            coEvery { projectRepository.getAlmostDoneProjects(3) } returns flow {
                throw Exception(errorMessage)
            }
            coEvery { miniatureRepository.getLastUpdatedMiniatures(3) } returns flow {
                throw Exception(errorMessage)
            }

            // WHEN
            viewModel.onAction(Load(true))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1){ projectRepository.getAlmostDoneProjects(3) }
            coVerify(exactly = 1){ miniatureRepository.getLastUpdatedMiniatures(3) }
            val state = viewModel.uiState.value
            assertTrue(state.error)
        }

    @Test
    fun `GIVEN user clicks project detail WHEN OnOpenProjectDetail is triggered THEN NavigateToProject event is sent`() =
        runTest {
            // GIVEN
            val projectId = 5L

            viewModel.events.test {
                // WHEN
                viewModel.onAction(OpenProjectDetail(projectId))

                // THEN
                val event = awaitItem()
                assertTrue(event is NavigateToProject)
                assertEquals(projectId, (event as NavigateToProject).projectId)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN user clicks start painting WHEN OnStartPainting is triggered THEN LaunchStartPaintModal event is sent`() =
        runTest {
            viewModel.events.test {
                // WHEN
                viewModel.onAction(StartPainting)

                // THEN
                assertEquals(
                    NavigateToStartPaint,
                    awaitItem()
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN user clicks add project WHEN OnAddProject is triggered THEN NavigateToAddProject event is sent`() =
        runTest {
            viewModel.events.test {
                // WHEN
                viewModel.onAction(AddProject)

                // THEN
                assertEquals(
                    NavigateToAddProject,
                    awaitItem()
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN empty projects WHEN OnUploadGamificationMessage is triggered THEN no message is raised`() =
        runTest {
            // GIVEN
            val projects = emptyList<ProjectBo>()
            val almostDoneProjects = emptyList<ProjectBo>()

            // WHEN
            viewModel.onAction(UpdateGamificationMessage(projects, almostDoneProjects))
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
            viewModel.onAction(UpdateGamificationMessage(projects, almostDoneProjects))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(EmptyProjects, state.gamificationSentence)
        }

    @Test
    fun `GIVEN 20 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = listOf(project50.copy(progress = 0.18f))
            viewModel.onAction(UpdateGamificationMessage(projects, emptyList()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.2f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 50 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = listOf(project50.copy(progress = 0.28f))
            viewModel.onAction(UpdateGamificationMessage(projects, emptyList()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.5f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 70 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = listOf(project50.copy(progress = 0.58f))
            viewModel.onAction(UpdateGamificationMessage(projects, emptyList()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.7f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 90 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = listOf(project50.copy(progress = 0.78f))
            viewModel.onAction(UpdateGamificationMessage(projects, emptyList()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.9f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 99 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = listOf(project50.copy(progress = 0.98f))
            viewModel.onAction(UpdateGamificationMessage(projects, emptyList()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 0.99f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN 100 percent started projects WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = listOf(project50.copy(progress = 1.0f))
            viewModel.onAction(UpdateGamificationMessage(projects, emptyList()))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ProgressRange(progress = 1.0f), state.gamificationSentence)
        }

    @Test
    fun `GIVEN selected project WHEN OnUploadGamificationMessage is triggered THEN ProgressRange is raised`() =
        runTest {
            val projects = projects
            val almostDoneProjects = listOf(project50.copy(progress = 1.0f))

            viewModel.onAction(UpdateGamificationMessage(projects, almostDoneProjects))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(
                AlmostDone(projectName = project50.name),
                state.gamificationSentence
            )
        }
}