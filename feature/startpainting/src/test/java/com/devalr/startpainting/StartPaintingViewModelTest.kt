package com.devalr.startpainting


import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.startpainting.interactions.Action.*
import com.devalr.startpainting.interactions.ErrorType
import com.devalr.startpainting.interactions.Event
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigatePaintMiniatures
import com.devalr.startpainting.mapper.StartPaintMiniatureVoMapper
import com.devalr.startpainting.mapper.StartPaintProjectVoMapper
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
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
class StartPaintingViewModelTest {

    private val projectsRepository: ProjectRepository = mockk()
    private val projectVoMapper: StartPaintProjectVoMapper = mockk()
    private val miniatureVoMapper: StartPaintMiniatureVoMapper = mockk()

    private lateinit var viewModel: StartPaintingViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val miniId1 = 1L
    private val miniId2 = 2L
    private val miniature1 = MiniatureBo(id = miniId1, projectId = 1, name = "Space Marine")
    private val miniature2 =
        MiniatureBo(id = miniId2, projectId = 1, name = "Space Marine Intercessor")
    private val miniature1Vo =
        StartPaintMiniatureVo(id = miniId1, projectId = 1, name = "Space Marine")
    private val miniature2Vo =
        StartPaintMiniatureVo(id = miniId2, projectId = 1, name = "Space Marine Intercessor")
    private val miniatures = listOf(miniature1, miniature2)

    private val filledProject = ProjectBo(id = 1, name = "Warhammer Army", minis = miniatures)
    private val emptyProject = ProjectBo(id = 2, name = "Warhammer Army empty")
    private val miniaturesVo = listOf(miniature1Vo, miniature2Vo)

    private val filledProjectVo =
        StartPaintProjectVo(id = 1, name = "Warhammer Army", minis = miniaturesVo)


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StartPaintingViewModel(projectsRepository, projectVoMapper, miniatureVoMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `GIVEN a valid project WHEN OnAppear is triggered THEN state updates with the project and its miniatures`() =
        runTest {
            // GIVEN
            coEvery { projectsRepository.getAllProjects() } returns flowOf(
                listOf(filledProject, emptyProject)
            )
            coEvery { projectVoMapper.transform(filledProject) } returns filledProjectVo
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 1) { projectVoMapper.transform(filledProject) }
            coVerify(exactly = 0) { projectVoMapper.transform(emptyProject) } // shouldn't be called because we filter empty projects
            assertTrue(state.projectsLoaded)
            assertEquals(listOf(filledProjectVo), state.projectList)
        }

    @Test
    fun `GIVEN a error in database WHEN OnAppear is triggered THEN error is raised`() =
        runTest {
            // GIVEN
            coEvery { projectsRepository.getAllProjects() } returns flow {
                throw RuntimeException("Database error")
            }
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 0) { projectVoMapper.transform(any()) }
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
    fun `WHEN OnSelectMiniature is triggered THEN NavigateBack event is raised`() =
        runTest {
            // GIVEN
            coEvery { projectsRepository.getAllProjects() } returns flowOf(listOf(filledProject))
            coEvery { projectVoMapper.transform(filledProject) } returns filledProjectVo
            viewModel.onAction(OnAppear)
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnSelectMiniature(miniature1Vo))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertTrue(state.paintButtonEnabled)
            val updatedProject = filledProjectVo.copy(minis = listOf(miniature1Vo.copy(isSelected = true), miniature2Vo))
            assertEquals(listOf(updatedProject), state.projectList)
        }

    @Test
    fun `WHEN OnStartPainting is triggered and 2 minis were selected previously THEN NavigatePaintMiniatures event is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            coEvery { projectsRepository.getAllProjects() } returns flowOf(listOf(filledProject))
            coEvery { projectVoMapper.transform(filledProject) } returns filledProjectVo
            coEvery { miniatureVoMapper.transformReverse(miniature1Vo.copy(isSelected = true)) } returns miniature1
            coEvery { miniatureVoMapper.transformReverse(miniature2Vo.copy(isSelected = true)) } returns miniature2
            viewModel.onAction(OnAppear)
            advanceUntilIdle()
            viewModel.onAction(OnSelectMiniature(miniature1Vo))
            viewModel.onAction(OnSelectMiniature(miniature2Vo))
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnStartPainting)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 1) { projectVoMapper.transform(filledProject) }
            coVerify(exactly = 2) { miniatureVoMapper.transformReverse(any()) }
            assertEquals(1, events.size)
            assertEquals(NavigatePaintMiniatures(listOf(miniId1,miniId2)), events.first())
            job.cancel()
        }

    @Test
    fun `WHEN OnStartPainting is triggered and 0 minis were selected previously THEN error is raised`() =
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
            val state = viewModel.uiState.value
            coVerify(exactly = 0) { projectsRepository.getAllProjects() }
            coVerify(exactly = 0) { projectVoMapper.transform(filledProject) }
            coVerify(exactly = 0) { miniatureVoMapper.transformReverse(any()) }
            assertEquals(0, events.size)
            assertEquals(ErrorType.NoMinisToPaint, state.error)
            job.cancel()
        }

}