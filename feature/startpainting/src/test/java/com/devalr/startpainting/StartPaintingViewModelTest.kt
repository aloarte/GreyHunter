package com.devalr.startpainting


import app.cash.turbine.test
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import com.devalr.startpainting.interactions.Action.Load
import com.devalr.startpainting.interactions.Action.Return
import com.devalr.startpainting.interactions.Action.SelectMiniature
import com.devalr.startpainting.interactions.Action.StartPainting
import com.devalr.startpainting.interactions.ErrorType
import com.devalr.startpainting.interactions.Event.LaunchErrorSnackBar
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigateToPaintMiniatures
import com.devalr.startpainting.mapper.StartPaintMiniatureVoMapper
import com.devalr.startpainting.mapper.StartPaintProjectVoMapper
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
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
class StartPaintingViewModelTest {

    private val projectsRepository: ProjectRepository = mockk()
    private val projectVoMapper: StartPaintProjectVoMapper = mockk()
    private val miniatureVoMapper: StartPaintMiniatureVoMapper = mockk()
    private val tracer: AppTracer = mockk()

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
        every { tracer.log(any()) } just Runs
        viewModel =
            StartPaintingViewModel(tracer, projectsRepository, projectVoMapper, miniatureVoMapper)
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

            viewModel.uiState.test {
                // initial state
                awaitItem()

                // WHEN
                viewModel.onAction(Load)
                advanceUntilIdle()

                // THEN
                val state = awaitItem()
                assertTrue(state.projectsLoaded)
                assertEquals(listOf(filledProjectVo), state.projectList)
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 1) { projectVoMapper.transform(filledProject) }
            coVerify(exactly = 0) { projectVoMapper.transform(emptyProject) }
        }

    @Test
    fun `GIVEN a error in database WHEN OnAppear is triggered THEN error is raised`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs
            coEvery { projectsRepository.getAllProjects() } returns flow {
                throw RuntimeException("Database error")
            }

            viewModel.events.test {
                // WHEN
                viewModel.onAction(Load)
                advanceUntilIdle()

                // THEN
                assertEquals(LaunchErrorSnackBar(ErrorType.RetrievingDatabase), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 0) { projectVoMapper.transform(any()) }
        }

    @Test
    fun `WHEN Return is triggered THEN NavigateBack event is raised`() =
        runTest {
            viewModel.events.test {
                // WHEN
                viewModel.onAction(Return)

                // THEN
                assertEquals(NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `WHEN OnSelectMiniature is triggered THEN NavigateBack event is raised`() =
        runTest {
            // GIVEN
            coEvery { projectsRepository.getAllProjects() } returns flowOf(listOf(filledProject))
            coEvery { projectVoMapper.transform(filledProject) } returns filledProjectVo

            viewModel.uiState.test {
                // initial
                awaitItem()

                viewModel.onAction(Load)
                advanceUntilIdle()
                awaitItem()

                // WHEN
                viewModel.onAction(SelectMiniature(miniature1Vo))
                advanceUntilIdle()

                // THEN
                val state = awaitItem()
                assertTrue(state.paintButtonEnabled)

                val updatedProject = filledProjectVo.copy(
                    minis = listOf(
                        miniature1Vo.copy(isSelected = true),
                        miniature2Vo
                    )
                )
                assertEquals(listOf(updatedProject), state.projectList)

                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 1) { projectVoMapper.transform(filledProject) }
        }

    @Test
    fun `WHEN OnStartPainting is triggered and 2 minis were selected previously THEN NavigatePaintMiniatures event is raised`() =
        runTest {
            // GIVEN
            coEvery { projectsRepository.getAllProjects() } returns flowOf(listOf(filledProject))
            coEvery { projectVoMapper.transform(filledProject) } returns filledProjectVo
            coEvery {
                miniatureVoMapper.transformReverse(miniature1Vo.copy(isSelected = true))
            } returns miniature1
            coEvery {
                miniatureVoMapper.transformReverse(miniature2Vo.copy(isSelected = true))
            } returns miniature2

            viewModel.events.test {
                viewModel.onAction(Load)
                advanceUntilIdle()
                viewModel.onAction(SelectMiniature(miniature1Vo))
                viewModel.onAction(SelectMiniature(miniature2Vo))
                advanceUntilIdle()

                // WHEN
                viewModel.onAction(StartPainting)
                advanceUntilIdle()

                // THEN
                assertEquals(NavigateToPaintMiniatures(listOf(miniId1, miniId2)), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { projectsRepository.getAllProjects() }
            coVerify(exactly = 1) { projectVoMapper.transform(filledProject) }
            coVerify(exactly = 2) { miniatureVoMapper.transformReverse(any()) }
        }

    @Test
    fun `WHEN OnStartPainting is triggered and 0 minis were selected previously THEN error is raised`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs

            viewModel.events.test {
                // WHEN
                viewModel.onAction(StartPainting)
                advanceUntilIdle()

                // THEN
                assertEquals(LaunchErrorSnackBar(ErrorType.NoMinisToPaint), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 0) { projectsRepository.getAllProjects() }
            coVerify(exactly = 0) { projectVoMapper.transform(any()) }
            coVerify(exactly = 0) { miniatureVoMapper.transformReverse(any()) }
        }
}