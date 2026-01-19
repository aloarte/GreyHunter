package com.devalr.settings

import androidx.lifecycle.viewModelScope
import com.devalr.domain.SettingsRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.settings.interactions.Action
import com.devalr.settings.interactions.Action.OnAppear
import com.devalr.settings.interactions.Event
import com.devalr.settings.interactions.State
import kotlinx.coroutines.launch

class SettingsViewModel(
    val projectRepository: SettingsRepository
) : BaseViewModel<State, Action, Event>(initialState = State()) {
    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> onLoadScreen()
            is Action.OnChangeDarkMode -> TODO()
        }
    }

    private fun onLoadScreen() {
        viewModelScope.launch {
            /* projectRepository.getProject(projectId)
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


             */

        }
    }

}