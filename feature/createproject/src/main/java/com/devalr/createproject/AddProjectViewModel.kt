package com.devalr.createproject

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
import kotlinx.coroutines.launch

class AddProjectViewModel(val projectRepository: ProjectRepository) :
    BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            OnAppear -> {}
            is OnNameChanged -> updateState { copy(projectName = action.name) }
            is OnDescriptionChanged -> updateState { copy(projectDescription = action.description) }
            is OnImageChanged -> {}/* updateState {copy(projectName = action.image) }*/
            is OnAddProject -> addProject()
        }
    }

    private fun addProject() {
        with(uiState.value) {
            if (projectName.isNullOrEmpty()) {
                updateState { copy(errorType = ErrorType.EmptyTitle) }
                return
            }

            val projectBo = ProjectBo(
                name = projectName,
                description = projectDescription
            )

            viewModelScope.launch {
                val projectAdded = projectRepository.addProject(projectBo) > 0
                if (projectAdded) {
                    updateState { copy(errorType = null) }
                    sendEvent(OnAddedSuccessfully)
                } else {
                    updateState { copy(errorType = ErrorType.AddDatabase) }
                }
            }
        }

    }

}