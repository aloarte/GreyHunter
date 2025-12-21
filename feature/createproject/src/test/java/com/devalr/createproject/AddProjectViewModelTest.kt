package com.devalr.createproject

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import com.devalr.createproject.interactions.Action.OnAddProject
import com.devalr.createproject.interactions.Action.OnDescriptionChanged
import com.devalr.createproject.interactions.Action.OnImageChanged
import com.devalr.createproject.interactions.Action.OnNameChanged
import com.devalr.createproject.interactions.ErrorType
import com.devalr.createproject.interactions.Event
import com.devalr.createproject.interactions.Event.OnAddedSuccessfully
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.ProjectBo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddProjectViewModelTest {
    private val application: Application = mockk(relaxed = true)
    private val contentResolver: ContentResolver = mockk(relaxed = true)
    private val repository: ProjectRepository = mockk()
    private lateinit var viewModel: AddProjectViewModel
    private val testDispatcher = StandardTestDispatcher()

    companion object {
        private const val PROJECT_NAME = "Hierotek Circle"
        private const val PROJECT_DESCRIPTION = "Project description."
        private val projectBo = ProjectBo(name = PROJECT_NAME, description = PROJECT_DESCRIPTION)

    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { application.contentResolver } returns contentResolver
        mockkStatic(Uri::class)
        viewModel = AddProjectViewModel(application, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN user inputs name WHEN OnNameChanged is triggered THEN state updates with project name`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(OnNameChanged(PROJECT_NAME))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(PROJECT_NAME, state.projectName)
        }

    @Test
    fun `GIVEN user inputs description WHEN OnNameChanged is triggered THEN state updates with project description`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(OnDescriptionChanged(PROJECT_DESCRIPTION))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(PROJECT_DESCRIPTION, state.projectDescription)
        }

    @Test
    fun `GIVEN empty name WHEN add button is clicked THEN state updates with error message`() =
        runTest {
            viewModel.onAction(OnAddProject)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ErrorType.EmptyTitle, state.errorType)
            coVerify(exactly = 0) { repository.addProject(any()) }
        }

    @Test
    fun `GIVEN filled fields WHEN add button is clicked THEN OnAddedSuccessfully event is dispatched and database is updated`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            viewModel.onAction(OnNameChanged(PROJECT_NAME))
            viewModel.onAction(OnDescriptionChanged(PROJECT_DESCRIPTION))
            coEvery { repository.addProject(projectBo) } returns 1

            // WHEN
            viewModel.onAction(OnAddProject)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertNull(state.errorType)
            coVerify(exactly = 1) { repository.addProject(projectBo) }
            assertEquals(1, events.size)
            assertEquals(OnAddedSuccessfully, events.first())
            job.cancel()
        }

    @Test
    fun `GIVEN filled fields WHEN add button is clicked and the database fails THEN AddDatabase error is raised`() =
        runTest {
            // GIVEN
            val events = mutableListOf<Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }
            viewModel.onAction(OnNameChanged(PROJECT_NAME))
            viewModel.onAction(OnDescriptionChanged(PROJECT_DESCRIPTION))
            coEvery { repository.addProject(projectBo) } returns 0

            // WHEN
            viewModel.onAction(OnAddProject)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ErrorType.AddDatabase, state.errorType)
            coVerify(exactly = 1) { repository.addProject(projectBo) }
            assertEquals(0, events.size)
            assertNull(events.firstOrNull())
            job.cancel()
        }


    @Test
    fun `GIVEN a gallery URI WHEN OnImageChanged is triggered THEN persistable permission is taken`() =
        runTest {
            // GIVEN
            val galleryUriString =
                "content://com.android.providers.media.documents/document/image%3A123"
            val galleryUri: Uri = mockk()
            every { Uri.parse(galleryUriString) } returns galleryUri
            every { galleryUri.toString() } returns galleryUriString
            // WHEN
            viewModel.onAction(OnImageChanged(galleryUri))

            // THEN
            verify(exactly = 1) {
                contentResolver.takePersistableUriPermission(
                    galleryUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            val state = viewModel.uiState.value
            assertEquals(galleryUriString, state.projectImage)
        }

    @Test
    fun `GIVEN a camera URI WHEN OnImageChanged is triggered THEN persistable permission is NOT taken`() =
        runTest {
            // GIVEN
            val cameraUriString =
                "content://com.devalr.greyhunter.fileprovider/shared_images/photo.jpg"
            val cameraUri: Uri = mockk()
            every { Uri.parse(cameraUriString) } returns cameraUri
            every { cameraUri.toString() } returns cameraUriString

            // WHEN
            viewModel.onAction(OnImageChanged(cameraUri))

            // THEN
            verify(exactly = 0) {
                contentResolver.takePersistableUriPermission(any(), any())
            }
            val state = viewModel.uiState.value
            assertEquals(cameraUriString, state.projectImage)
        }
}