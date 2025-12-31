package com.devalr.createproject

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.devalr.createproject.interactions.Action
import com.devalr.createproject.interactions.Action.OnAddProject
import com.devalr.createproject.interactions.Action.OnAppear
import com.devalr.createproject.interactions.Action.OnDescriptionChanged
import com.devalr.createproject.interactions.Action.OnImageChanged
import com.devalr.createproject.interactions.Action.OnNameChanged
import com.devalr.createproject.interactions.ErrorType
import com.devalr.createproject.interactions.Event
import com.devalr.createproject.interactions.Event.OnAddedSuccessfully
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
            is OnAppear -> onLoadScreen(projectId = action.projectId)
            is OnNameChanged -> updateState { copy(projectName = action.name) }
            is OnDescriptionChanged -> updateState { copy(projectDescription = action.description) }
            is OnImageChanged -> updateImage(action.imageUri)
            is OnAddProject -> addEditProject()
        }
    }

    private fun onLoadScreen(projectId: Long?) {
        projectId?.let {
            viewModelScope.launch {
                projectRepository.getProject(projectId)
                    .catch { updateState { copy(errorType = ErrorType.BadId) } }
                    .collect {
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
                sendEvent(OnAddedSuccessfully)
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
            sendEvent(OnAddedSuccessfully)
        } else {
            updateState { copy(errorType = ErrorType.AddDatabase) }
        }
    }

}