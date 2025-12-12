package com.devalr.projectdetail

import org.junit.Test

import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.ProjectBo
import com.devalr.projectdetail.interactions.Action.OnAppear
import io.mockk.coEvery
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectDetailViewModelTest {

    private val repository: ProjectRepository = mockk()
    private lateinit var viewModel: ProjectDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val projectId = 10L
    private val project = ProjectBo(
        id = projectId,
        name = "Hierotek Circle",
        percentage = 0.5f,
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
    fun `GIVEN a project id WHEN OnAppear action is triggered THEN state updates with project data`() = runTest {
        // GIVEN
        coEvery { repository.getProject(projectId) } returns flowOf(project)

        // WHEN
        viewModel.onAction(OnAppear(projectId))
        advanceUntilIdle()

        // THEN
        val currentState = viewModel.uiState.value
        assertTrue(currentState.projectLoaded)
        assertEquals(project, currentState.project)
        assertNull(currentState.error)
    }

    @Test
    fun `GIVEN a repository error WHEN OnAppear action is triggered THEN state updates with error message`() = runTest {
        // GIVEN
        val errorMessage = "Database connection failed"
        coEvery { repository.getProject(projectId) } returns flow {
            throw Exception(errorMessage)
        }

        // WHEN
        viewModel.onAction(OnAppear(projectId))
        advanceUntilIdle()

        // THEN
        val currentState = viewModel.uiState.value
        assertEquals(errorMessage, currentState.error)
    }
}