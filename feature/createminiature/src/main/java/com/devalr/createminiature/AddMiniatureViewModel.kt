package com.devalr.createminiature

import android.app.Application
import android.content.Intent
import android.util.Log
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
import kotlinx.coroutines.launch

class AddMiniatureViewModel(
    private val application: Application,
    val miniatureRepository: MiniatureRepository,
    val projectRepository: ProjectRepository
) :
    BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> updateState { copy(projectId = action.projectId) }
            is OnNameChanged -> updateState { copy(miniatureName = action.name) }
            is OnImageChanged -> {
                try {
                    if (!action.imageUri.toString().contains(".fileprovider")) {
                        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        application.contentResolver.takePersistableUriPermission(action.imageUri, flags)
                    }
                    updateState { copy(miniatureImage = action.imageUri.toString()) }

                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            is OnAddMiniature -> addMiniature()
        }
    }

    private fun addMiniature() {
        with(uiState.value) {
            if (projectId == null) {
                updateState { copy(errorType = ErrorType.EmptyTitle) }
                return
            }
            if (miniatureName.isNullOrEmpty()) {
                updateState { copy(errorType = ErrorType.EmptyTitle) }
                return
            }

            val miniature = MiniatureBo(
                projectId = projectId,
                name = miniatureName,
                imageUri = miniatureImage
            )

            viewModelScope.launch {
                val miniatureAdded = miniatureRepository.addMiniature(miniature) > 0
                if (miniatureAdded) {
                    val projectUpdated =
                        projectRepository.updateProjectProgress(projectId = projectId)
                    updateState { copy(errorType = if (projectUpdated) null else ErrorType.ErrorUpdatingProgress) }
                    sendEvent(OnAddedSuccessfully)

                } else {
                    updateState { copy(errorType = ErrorType.AddDatabase) }
                }
            }
        }
    }
}