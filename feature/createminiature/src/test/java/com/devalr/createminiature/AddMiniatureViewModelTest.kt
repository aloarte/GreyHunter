package com.devalr.createminiature

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import com.devalr.createminiature.interactions.Action.OnAddMiniature
import com.devalr.createminiature.interactions.Action.OnAppear
import com.devalr.createminiature.interactions.Action.OnImageChanged
import com.devalr.createminiature.interactions.Action.OnNameChanged
import com.devalr.createminiature.interactions.ErrorType
import com.devalr.createminiature.interactions.Event.OnAddedSuccessfully
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class AddMiniatureViewModelTest {

    private val application: Application = mockk(relaxed = true)
    private val contentResolver: ContentResolver = mockk(relaxed = true)
    private val miniatureRepository: MiniatureRepository = mockk()
    private val projectRepository: ProjectRepository = mockk()
    private lateinit var viewModel: AddMiniatureViewModel
    private val testDispatcher = StandardTestDispatcher()

    companion object {
        const val PROJECT_ID = 10L
        const val MINI_ID = 1L
        const val MINI_NAME = "Space marine"
        const val MINI_IMAGE = "URI/example/1"
        const val NEW_MINI_NAME = "Space Assaulter"
        const val NEW_MINI_IMAGE = "URI/example/2"
        val existentMini = MiniatureBo(
            id = MINI_ID,
            projectId = PROJECT_ID,
            name = MINI_NAME,
            imageUri = MINI_IMAGE
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { application.contentResolver } returns contentResolver
        mockkStatic(Uri::class)
        viewModel = AddMiniatureViewModel(application, miniatureRepository, projectRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN initialized THEN fields are empty`() = runTest {
        val state = viewModel.uiState.value
        assertNull(state.projectId)
        assertNull(state.miniatureName)
        assertNull(state.errorType)
    }

    @Test
    fun `GIVEN a projectId and empty miniId WHEN OnAppear is triggered THEN state updates with projectId`() =
        runTest {
            // GIVEN & WHEN
            viewModel.onAction(OnAppear(projectId = PROJECT_ID, miniatureId = -1))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 0) { miniatureRepository.getMiniature(any()) }
            assertEquals(PROJECT_ID, state.projectId)
            assertFalse(state.editMode)
        }

    @Test
    fun `GIVEN a projectId and miniId WHEN OnAppear is triggered THEN state updates with projectId and a miniature to update`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.getMiniature(MINI_ID) } returns flow { emit(existentMini) }

            // WHEN
            viewModel.onAction(OnAppear(projectId = PROJECT_ID, miniatureId = MINI_ID))
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(PROJECT_ID, state.projectId)
            assertEquals(MINI_NAME, state.miniatureName)
            assertEquals(MINI_IMAGE, state.miniatureImage)
            assertEquals(existentMini, state.miniatureToUpdate)
            assertTrue(state.editMode)
            coVerify(exactly = 1) { miniatureRepository.getMiniature(MINI_ID) }
        }

    @Test
    fun `GIVEN user inputs name WHEN OnNameChanged is triggered THEN state updates with miniature name`() =
        runTest {
            viewModel.onAction(OnNameChanged(MINI_NAME))

            val state = viewModel.uiState.value
            assertEquals(MINI_NAME, state.miniatureName)
        }

    @Test
    fun `GIVEN missing project ID WHEN OnAddMiniature is triggered THEN EmptyTitle error is set`() =
        runTest {
            // GIVEN
            viewModel.onAction(OnNameChanged(MINI_NAME))

            // WHEN
            viewModel.onAction(OnAddMiniature)

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ErrorType.BadId, state.errorType)
            coVerify(exactly = 0) { miniatureRepository.addMiniature(any()) }
        }

    @Test
    fun `GIVEN empty name WHEN OnAddMiniature is triggered THEN EmptyTitle error is set`() =
        runTest {
            // GIVEN
            viewModel.onAction(OnAppear(projectId = PROJECT_ID))
            viewModel.onAction(OnNameChanged(""))

            // WHEN
            viewModel.onAction(OnAddMiniature)

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ErrorType.EmptyTitle, state.errorType)
            coVerify(exactly = 0) { miniatureRepository.addMiniature(any()) }
        }

    @Test
    fun `GIVEN database error WHEN OnAddMiniature is triggered THEN AddDatabase error is set`() =
        runTest {
            // GIVEN
            viewModel.onAction(OnAppear(projectId = PROJECT_ID))
            viewModel.onAction(OnNameChanged(MINI_NAME))
            coEvery { miniatureRepository.addMiniature(any()) } returns 0L

            // WHEN
            viewModel.onAction(OnAddMiniature)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            assertEquals(ErrorType.AddDatabase, state.errorType)
        }

    @Test
    fun `GIVEN successful add but failed progress update WHEN OnAddMiniature is triggered THEN ErrorUpdatingProgress is set`() =
        runTest {
            // GIVEN
            viewModel.onAction(OnAppear(projectId = PROJECT_ID))
            viewModel.onAction(OnNameChanged(MINI_NAME))
            coEvery { miniatureRepository.addMiniature(any()) } returns 1L
            coEvery { projectRepository.updateProjectProgress(any()) } returns false

            // WHEN
            viewModel.onAction(OnAddMiniature)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { miniatureRepository.addMiniature(any()) }
            coVerify(exactly = 0) { miniatureRepository.updateMiniature(any()) }
            assertEquals(ErrorType.ErrorUpdatingProgress, state.errorType)
        }

    @Test
    fun `GIVEN successful add and progress update WHEN OnAddMiniature is triggered THEN success event is sent`() =
        runTest {
            // GIVEN
            viewModel.onAction(OnAppear(projectId = PROJECT_ID))
            viewModel.onAction(OnNameChanged(MINI_NAME))
            coEvery { miniatureRepository.addMiniature(any()) } returns 10L
            coEvery { projectRepository.updateProjectProgress(PROJECT_ID) } returns true
            val events = mutableListOf<com.devalr.createminiature.interactions.Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnAddMiniature)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { miniatureRepository.addMiniature(any()) }
            coVerify(exactly = 0) { miniatureRepository.updateMiniature(any()) }
            coVerify(exactly = 1) { projectRepository.updateProjectProgress(PROJECT_ID) }
            assertNull(viewModel.uiState.value.errorType)
            assertEquals(1, events.size)
            assertTrue(events.contains(OnAddedSuccessfully))
            job.cancel()
        }

    @Test
    fun `GIVEN successful edit but failed progress update WHEN OnAddMiniature is triggered THEN ErrorUpdatingProgress is set`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.getMiniature(MINI_ID) } returns flow { emit(existentMini) }
            coEvery { miniatureRepository.updateMiniature(any()) } returns true
            coEvery { projectRepository.updateProjectProgress(any()) } returns false
            viewModel.onAction(OnAppear(projectId = PROJECT_ID, miniatureId = MINI_ID))
            viewModel.onAction(OnNameChanged(NEW_MINI_NAME))
            advanceUntilIdle()

            // WHEN
            viewModel.onAction(OnAddMiniature)
            advanceUntilIdle()

            // THEN
            val state = viewModel.uiState.value
            coVerify(exactly = 1) { miniatureRepository.getMiniature(MINI_ID) }
            coVerify(exactly = 0) { miniatureRepository.addMiniature(any()) }
            coVerify(exactly = 1) { miniatureRepository.updateMiniature(any()) }
            assertEquals(ErrorType.ErrorUpdatingProgress, state.errorType)
        }

    @Test
    fun `GIVEN successful edit and progress update WHEN OnAddMiniature is triggered THEN success event is sent`() =
        runTest {
            // GIVEN
            coEvery { miniatureRepository.getMiniature(MINI_ID) } returns flow { emit(existentMini) }
            coEvery { miniatureRepository.updateMiniature(any()) } returns true
            coEvery { projectRepository.updateProjectProgress(PROJECT_ID) } returns true
            viewModel.onAction(OnAppear(projectId = PROJECT_ID, miniatureId = MINI_ID))
            viewModel.onAction(OnNameChanged(NEW_MINI_NAME))
            viewModel.onAction(OnNameChanged(NEW_MINI_IMAGE))

            advanceUntilIdle()
            val events = mutableListOf<com.devalr.createminiature.interactions.Event>()
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.collect { events.add(it) }
            }

            // WHEN
            viewModel.onAction(OnAddMiniature)
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { miniatureRepository.getMiniature(MINI_ID) }
            coVerify(exactly = 0) { miniatureRepository.addMiniature(any()) }
            coVerify(exactly = 1) { miniatureRepository.updateMiniature(any()) }
            coVerify(exactly = 1) { projectRepository.updateProjectProgress(PROJECT_ID) }
            assertNull(viewModel.uiState.value.errorType)
            assertEquals(1, events.size)
            assertTrue(events.contains(OnAddedSuccessfully))
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
            assertEquals(galleryUriString, state.miniatureImage)
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
            assertEquals(cameraUriString, state.miniatureImage)
        }
}