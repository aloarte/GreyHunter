package com.devalr.createminiature

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.devalr.createminiature.interactions.Action
import com.devalr.createminiature.interactions.Action.OnAddMiniature
import com.devalr.createminiature.interactions.Action.OnAppear
import com.devalr.createminiature.interactions.Action.OnImageChanged
import com.devalr.createminiature.interactions.Action.OnNameChanged
import com.devalr.createminiature.interactions.ErrorType
import com.devalr.createminiature.interactions.Event
import com.devalr.createminiature.interactions.Event.OnAddedSuccessfully
import com.devalr.createminiature.interactions.State
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddMiniatureViewModel(
    private val application: Application,
    val miniatureRepository: MiniatureRepository,
    val projectRepository: ProjectRepository
) :
    BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> onLoadScreen(
                projectId = action.projectId,
                miniatureId = action.miniatureId
            )
            is OnNameChanged -> updateState { copy(miniatureName = action.name) }
            is OnImageChanged -> updateImage(action.imageUri)
            is OnAddMiniature -> addEditMiniature()
        }
    }

    private fun updateImage(imageUri: Uri) {
        try {
            if (!imageUri.toString().contains(".fileprovider")) {
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                application.contentResolver.takePersistableUriPermission(
                    imageUri,
                    flags
                )
            }
            updateState { copy(miniatureImage = imageUri.toString()) }

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun onLoadScreen(miniatureId: Long, projectId: Long) {
        if (miniatureId > 0) {
            viewModelScope.launch {
                miniatureRepository.getMiniature(miniatureId)
                    .catch {
                        updateState { copy(errorType = ErrorType.BadId) }
                    }
                    .collect {
                        updateState {
                            copy(
                                projectId = it.projectId,
                                miniatureName = it.name,
                                miniatureImage = it.imageUri,
                                miniatureToUpdate = it,
                                editMode = true
                            )
                        }
                    }
            }
        } else {
            updateState { copy(projectId = projectId) }
        }
    }

    private fun addEditMiniature() {
        with(uiState.value) {
            if (projectId == null) {
                updateState { copy(errorType = ErrorType.BadId) }
                return
            }
            if (miniatureName.isNullOrEmpty()) {
                updateState { copy(errorType = ErrorType.EmptyTitle) }
                return
            }

            if (editMode) {
                editMiniature(
                    miniatureToUpdate = miniatureToUpdate,
                    projectId = projectId,
                    miniatureName = miniatureName,
                    miniatureImage = miniatureImage
                )
            } else {
                val miniature = MiniatureBo(
                    projectId = projectId,
                    name = miniatureName,
                    imageUri = miniatureImage
                )
                addMiniature(miniature = miniature, projectId = projectId)
            }
        }
    }

    private fun addMiniature(miniature: MiniatureBo, projectId: Long) =
        viewModelScope.launch {
            val miniatureAdded = miniatureRepository.addMiniature(miniature) > 0
            if (miniatureAdded) {
                val projectUpdated = projectRepository.updateProjectProgress(projectId = projectId)
                updateState { copy(errorType = if (projectUpdated) null else ErrorType.ErrorUpdatingProgress) }
                sendEvent(OnAddedSuccessfully)

            } else {
                updateState { copy(errorType = ErrorType.AddDatabase) }
            }
        }

    private fun editMiniature(
        miniatureToUpdate: MiniatureBo?,
        projectId: Long,
        miniatureName: String,
        miniatureImage: String?
    ) =
        viewModelScope.launch {
            miniatureToUpdate?.let {
                val newMiniature =
                    miniatureToUpdate.copy(name = miniatureName, imageUri = miniatureImage)
                if (miniatureRepository.updateMiniature(newMiniature)) {
                    val projectUpdated =
                        projectRepository.updateProjectProgress(projectId = projectId)
                    updateState { copy(errorType = if (projectUpdated) null else ErrorType.ErrorUpdatingProgress) }
                    sendEvent(OnAddedSuccessfully)

                } else {
                    updateState { copy(errorType = ErrorType.EditDatabase) }
                }
            } ?: run {
                updateState { copy(errorType = ErrorType.BadId) }
            }
        }
}