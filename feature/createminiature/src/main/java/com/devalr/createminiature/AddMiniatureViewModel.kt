package com.devalr.createminiature

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.devalr.createminiature.interactions.Action
import com.devalr.createminiature.interactions.Action.AddMiniature
import com.devalr.createminiature.interactions.Action.ChangeImage
import com.devalr.createminiature.interactions.Action.ChangeName
import com.devalr.createminiature.interactions.Action.Load
import com.devalr.createminiature.interactions.Action.Return
import com.devalr.createminiature.interactions.ErrorType
import com.devalr.createminiature.interactions.ErrorType.AddDatabase
import com.devalr.createminiature.interactions.ErrorType.BadId
import com.devalr.createminiature.interactions.ErrorType.EditDatabase
import com.devalr.createminiature.interactions.ErrorType.EmptyName
import com.devalr.createminiature.interactions.ErrorType.ErrorUpdatingProgress
import com.devalr.createminiature.interactions.ErrorType.ImportImage
import com.devalr.createminiature.interactions.Event
import com.devalr.createminiature.interactions.Event.LaunchSnackBarError
import com.devalr.createminiature.interactions.Event.NavigateBack
import com.devalr.createminiature.interactions.State
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.AppTracer
import com.devalr.framework.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class AddMiniatureViewModel(
    private val application: Application,
    private val tracer: AppTracer,
    private val miniatureRepository: MiniatureRepository,
    private val projectRepository: ProjectRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        tracer.log("AddMiniatureViewModel.onAction: ${action::class.simpleName}")
        when (action) {
            is Load -> onLoadScreen(
                miniatureId = action.miniatureId,
                projectId = action.projectId
            )

            is ChangeName -> updateState { copy(miniatureName = action.name) }
            is ChangeImage -> updateImage(action.imageUri)
            is AddMiniature -> addEditMiniature()
            is Return -> sendEvent(NavigateBack)
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
            submitError(e, ImportImage)
        }
    }

    private fun onLoadScreen(projectId: Long, miniatureId: Long?) {
        if (miniatureId != null) {
            viewModelScope.launch {
                combine(
                    miniatureRepository.getMiniature(miniatureId),
                    projectRepository.getProject(projectId)
                ) { miniature, project ->
                    if (miniature == null || project == null) {
                        null
                    } else {
                        uiState.value.copy(
                            projectId = miniature.projectId,
                            projectName = project.name,
                            miniatureName = miniature.name,
                            miniatureImage = miniature.imageUri,
                            miniatureToUpdate = miniature,
                            editMode = true
                        )
                    }
                }.filterNotNull().catch { error ->
                    updateState { copy(error = true) }
                    submitError(error, BadId)
                }.collect { newState ->
                    updateState { newState }
                }
            }
        } else {
            viewModelScope.launch {
                projectRepository.getProject(projectId).collect { project ->
                    updateState {
                        copy(
                            projectId = projectId,
                            projectName = project?.name,
                            editMode = false
                        )
                    }
                }
            }
        }
    }

    private fun addEditMiniature() {
        with(uiState.value) {
            if (projectId == null) {
                submitError(Exception("addEditMiniature null projectId"), BadId)
                return
            }
            if (miniatureName.isNullOrEmpty()) {
                submitError(Exception("addEditMiniature null miniatureName"), EmptyName)
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
                val projectUpdated = projectRepository.updateProjectProgress(
                    projectId = projectId,
                    avoidLastUpdate = true
                )
                if (!projectUpdated) {
                    submitError(
                        Exception("addMiniature project update error"),
                        ErrorUpdatingProgress
                    )
                } else {
                    sendEvent(NavigateBack)
                }
            } else submitError(
                Exception("addMiniature Miniature not added"),
                AddDatabase
            )
        }


    private fun editMiniature(
        miniatureToUpdate: MiniatureBo?,
        projectId: Long,
        miniatureName: String,
        miniatureImage: String?
    ) = viewModelScope.launch {
        miniatureToUpdate?.let {
            val newMiniature =
                miniatureToUpdate.copy(name = miniatureName, imageUri = miniatureImage)
            if (miniatureRepository.updateMiniature(newMiniature)) {
                val projectUpdated =
                    projectRepository.updateProjectProgress(projectId = projectId)
                if (!projectUpdated) {
                    submitError(
                        Exception("editMiniature project update error"),
                        ErrorUpdatingProgress
                    )
                } else {
                    sendEvent(NavigateBack)
                }
            } else {
                submitError(Exception("editMiniature database update error"), EditDatabase)
            }
        } ?: run {
            submitError(Exception("editMiniature null miniature to updater"), BadId)
        }
    }

    private fun submitError(error: Throwable, errorType: ErrorType? = null) {
        tracer.recordError(error)
        errorType?.let { sendEvent(LaunchSnackBarError(errorType)) }
    }

}