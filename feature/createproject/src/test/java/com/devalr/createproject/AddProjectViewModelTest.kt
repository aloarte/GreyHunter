package com.devalr.createproject

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import app.cash.turbine.test
import com.devalr.createproject.interactions.Action.AddProject
import com.devalr.createproject.interactions.Action.ChangeDescription
import com.devalr.createproject.interactions.Action.ChangeImage
import com.devalr.createproject.interactions.Action.ChangeName
import com.devalr.createproject.interactions.Action.Load
import com.devalr.createproject.interactions.Action.Return
import com.devalr.createproject.interactions.ErrorType.AddDatabase
import com.devalr.createproject.interactions.ErrorType.EditDatabase
import com.devalr.createproject.interactions.ErrorType.EmptyTitle
import com.devalr.createproject.interactions.Event.LaunchSnackBarError
import com.devalr.createproject.interactions.Event.NavigateBack
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.AppTracer
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddProjectViewModelTest {

    private val application: Application = mockk(relaxed = true)
    private val contentResolver: ContentResolver = mockk(relaxed = true)
    private val tracer: AppTracer = mockk()
    private val repository: ProjectRepository = mockk()
    private lateinit var viewModel: AddProjectViewModel
    private val testDispatcher = StandardTestDispatcher()

    companion object {
        private const val PROJECT_ID = 10L
        private const val PROJECT_NAME = "Hierotek Circle"
        private const val PROJECT_DESCRIPTION = "Project description."
        private const val PROJECT_IMAGE =
            "content://com.devalr.greyhunter.fileprovider/shared_images/photo.jpg"
        private val projectBo = ProjectBo(
            id = PROJECT_ID,
            name = PROJECT_NAME,
            description = PROJECT_DESCRIPTION,
            imageUri = PROJECT_IMAGE
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { application.contentResolver } returns contentResolver
        every { tracer.log(any()) } just Runs
        mockkStatic(Uri::class)
        viewModel = AddProjectViewModel(application, tracer, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN existent project WHEN OnAppear THEN project is loaded from database`() =
        runTest {
            // GIVEN
            coEvery { repository.getProject(PROJECT_ID) } returns flow { emit(projectBo) }

            // WHEN
            viewModel.onAction(Load(projectId = PROJECT_ID))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { repository.getProject(PROJECT_ID) }
            assertEquals(PROJECT_NAME, state.projectName)
            assertEquals(PROJECT_DESCRIPTION, state.projectDescription)
            assertEquals(PROJECT_IMAGE, state.projectImage)
            assertEquals(projectBo, state.projectToUpdate)
            assertTrue(state.editMode)
        }

    @Test
    fun `GIVEN new project WHEN OnAppear THEN project is not loaded from database`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(Load())

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 0) { repository.getProject(PROJECT_ID) }
            assertNull(state.projectName)
            assertNull(state.projectDescription)
            assertNull(state.projectImage)
            assertNull(state.projectToUpdate)
            assertFalse(state.editMode)
        }

    @Test
    fun `GIVEN user inputs name WHEN OnNameChanged is triggered THEN state updates with project name`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(ChangeName(PROJECT_NAME))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(PROJECT_NAME, state.projectName)
        }

    @Test
    fun `GIVEN user inputs description WHEN OnNameChanged is triggered THEN state updates with project description`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(ChangeDescription(PROJECT_DESCRIPTION))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(PROJECT_DESCRIPTION, state.projectDescription)
        }

    @Test
    fun `GIVEN empty name WHEN add button is clicked THEN state updates with error message`() =
        runTest {
            // GIVEN
            every { tracer.recordError(any()) } just Runs

            viewModel.events.test {
                // WHEN
                viewModel.onAction(AddProject)

                // THEN
                assertEquals(LaunchSnackBarError(EmptyTitle), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            //THEN
            verify(exactly = 1) { tracer.recordError(any()) }
            coVerify(exactly = 0) { repository.addProject(any()) }
        }

    @Test
    fun `GIVEN filled fields WHEN add button is clicked THEN OnAddedSuccessfully event is dispatched and database is updated`() =
        runTest {
            // GIVEN
            coEvery { repository.addProject(any()) } returns 1

            viewModel.events.test {
                // GIVEN
                viewModel.onAction(ChangeName(PROJECT_NAME))
                viewModel.onAction(ChangeDescription(PROJECT_DESCRIPTION))
                advanceUntilIdle()

                // WHEN
                viewModel.onAction(AddProject)
                advanceUntilIdle()

                // THEN
                assertEquals(NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            // THEN
            coVerify(exactly = 1) { repository.addProject(any()) }
            coVerify(exactly = 0) { repository.updateProject(any()) }

        }

    @Test
    fun `GIVEN filled fields WHEN add button is clicked and the database fails THEN AddDatabase error is raised`() =
        runTest {
            // GIVEN
            coEvery { repository.addProject(any()) } returns 0
            every { tracer.recordError(any()) } just Runs

            viewModel.events.test {
                // GIVEN
                viewModel.onAction(ChangeName(PROJECT_NAME))
                viewModel.onAction(ChangeDescription(PROJECT_DESCRIPTION))

                // WHEN
                viewModel.onAction(AddProject)

                // THEN
                assertEquals(LaunchSnackBarError(AddDatabase), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.addProject(any()) }
            coVerify(exactly = 0) { repository.updateProject(any()) }
        }

    @Test
    fun `GIVEN filled fields WHEN edit button is clicked and the database fails THEN AddDatabase error is raised`() =
        runTest {
            // GIVEN
            coEvery { repository.getProject(PROJECT_ID) } returns flow { emit(projectBo) }
            coEvery { repository.updateProject(any()) } returns false
            every { tracer.recordError(any()) } just Runs

            viewModel.events.test {
                // GIVEN
                viewModel.onAction(Load(PROJECT_ID))
                viewModel.onAction(ChangeName(PROJECT_NAME))
                viewModel.onAction(ChangeDescription(PROJECT_DESCRIPTION))
                advanceUntilIdle()

                // WHEN
                viewModel.onAction(AddProject)
                advanceUntilIdle()

                // THEN
                assertEquals(LaunchSnackBarError(EditDatabase), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.getProject(PROJECT_ID) }
            coVerify(exactly = 0) { repository.addProject(any()) }
            coVerify(exactly = 1) { repository.updateProject(any()) }
        }

    @Test
    fun `GIVEN filled fields WHEN edit button is clicked THEN event is dispatched and database is updated`() =
        runTest {
            // GIVEN
            coEvery { repository.getProject(PROJECT_ID) } returns flow { emit(projectBo) }
            coEvery { repository.updateProject(any()) } returns true

            viewModel.events.test {
                // GIVEN
                viewModel.onAction(Load(PROJECT_ID))
                viewModel.onAction(ChangeName(PROJECT_NAME))
                viewModel.onAction(ChangeDescription(PROJECT_DESCRIPTION))
                advanceUntilIdle()

                // WHEN
                viewModel.onAction(AddProject)
                advanceUntilIdle()

                // THEN
                assertEquals(NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // THEN
            coVerify(exactly = 1) { repository.getProject(PROJECT_ID) }
            coVerify(exactly = 0) { repository.addProject(any()) }
            coVerify(exactly = 1) { repository.updateProject(any()) }
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
            viewModel.onAction(ChangeImage(galleryUri))

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
            viewModel.onAction(ChangeImage(cameraUri))

            // THEN
            verify(exactly = 0) { contentResolver.takePersistableUriPermission(any(), any()) }
            val state = viewModel.uiState.value
            assertEquals(cameraUriString, state.projectImage)
        }

    @Test
    fun `WHEN Return action is triggered THEN NavigateBack event is raised`() =
        runTest {
            viewModel.events.test {
                // WHEN
                viewModel.onAction(Return)

                // THEN
                assertEquals(
                    NavigateBack,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
}