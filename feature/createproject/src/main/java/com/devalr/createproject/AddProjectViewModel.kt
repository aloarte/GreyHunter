package com.devalr.createproject

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.devalr.createproject.interactions.Action
import com.devalr.createproject.interactions.Action.AddProject
import com.devalr.createproject.interactions.Action.Load
import com.devalr.createproject.interactions.Action.ChangeDescription
import com.devalr.createproject.interactions.Action.ChangeImage
import com.devalr.createproject.interactions.Action.ChangeName
import com.devalr.createproject.interactions.ErrorType
import com.devalr.createproject.interactions.Event
import com.devalr.createproject.interactions.Event.NavigateBack
import com.devalr.createproject.interactions.State
import com.devalr.domain.ProjectRepository
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddProjectViewModel(
    private val application: Application,
    val projectRepository: ProjectRepository
) :
    BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is Load -> onLoadScreen(projectId = action.projectId)
            is ChangeName -> updateState { copy(projectName = action.name) }
            is ChangeDescription -> updateState { copy(projectDescription = action.description) }
            is ChangeImage -> updateImage(action.imageUri)
            is AddProject -> addEditProject()
        }
    }

    private fun onLoadScreen(projectId: Long?) {
        projectId?.let {
            viewModelScope.launch {
                projectRepository.getProject(projectId)
                    .catch { updateState { copy(errorType = ErrorType.BadId) } }
                    .collect {
                        if (it == null) return@collect
                        updateState {
                            copy(
                                projectDescription = it.description,
                                projectName = it.name,
                                projectImage = it.imageUri,
                                projectToUpdate = it,
                                editMode = true
                            )
                        }
                    }
            }
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
            updateState { copy(projectImage = imageUri.toString()) }

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }


    private fun addEditProject() {
        with(uiState.value) {
            if (projectName.isNullOrEmpty()) {
                updateState { copy(errorType = ErrorType.EmptyTitle) }
                return
            }

            if (editMode) {
                editProject(
                    projectToUpdate = projectToUpdate,
                    newProjectName = projectName,
                    newProjectDescription = projectDescription,
                    newProjectImage = projectImage
                )
            } else {
                val projectBo = ProjectBo(
                    name = projectName,
                    description = projectDescription,
                    imageUri = projectImage
                )
                addProject(projectBo)
            }
        }
    }

    private fun editProject(
        projectToUpdate: ProjectBo?,
        newProjectName: String,
        newProjectDescription: String?,
        newProjectImage: String?
    ) = viewModelScope.launch {
        projectToUpdate?.let {
            val updatedProject =
                projectToUpdate.copy(
                    name = newProjectName,
                    description = newProjectDescription,
                    imageUri = newProjectImage
                )
            if (projectRepository.updateProject(updatedProject)) {
                updateState { copy(errorType = null) }
                sendEvent(NavigateBack)
            } else {
                updateState { copy(errorType = ErrorType.EditDatabase) }
            }
        } ?: run {
            updateState { copy(errorType = ErrorType.BadId) }
        }
    }

    private fun addProject(project: ProjectBo) = viewModelScope.launch {
        val projectAdded = projectRepository.addProject(project) > 0
        if (projectAdded) {
            updateState { copy(errorType = null) }
            sendEvent(NavigateBack)
        } else {
            updateState { copy(errorType = ErrorType.AddDatabase) }
        }
    }

}