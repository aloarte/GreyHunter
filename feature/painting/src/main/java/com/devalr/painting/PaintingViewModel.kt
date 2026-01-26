package com.devalr.painting

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.painting.interactions.Action
import com.devalr.painting.interactions.Action.FinishPainting
import com.devalr.painting.interactions.Action.Load
import com.devalr.painting.interactions.Action.Return
import com.devalr.painting.interactions.ErrorType
import com.devalr.painting.interactions.Event
import com.devalr.painting.interactions.Event.NavigateBack
import com.devalr.painting.interactions.Event.NavigateToUpdateMiniatures
import com.devalr.painting.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PaintingViewModel(val minisRepository: MiniatureRepository) :
    BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is Load -> fetchMiniatures(action.minisIds)
            Return -> sendEvent(NavigateBack)
            is FinishPainting -> sendEvent(NavigateToUpdateMiniatures(action.miniatureIds))
        }
    }

    private fun fetchMiniatures(minisIds: List<Long>) {
        viewModelScope.launch {
            minisRepository.getMiniatures(miniaturesId = minisIds)
                .catch { updateState { copy(error = ErrorType.RetrievingDatabase) } }
                .collect {
                    updateState {
                        copy(
                            miniatures = it,
                            minisLoaded = true
                        )
                    }
                }

        }
    }
}