package com.devalr.painting

import androidx.lifecycle.viewModelScope
import com.devalr.domain.MiniatureRepository
import com.devalr.framework.base.BaseViewModel
import com.devalr.painting.interactions.Action
import com.devalr.painting.interactions.Action.OnAppear
import com.devalr.painting.interactions.Event
import com.devalr.painting.interactions.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PaintingViewModel(val minisRepository: MiniatureRepository) :
    BaseViewModel<State, Action, Event>(initialState = State()) {

    override fun onAction(action: Action) {
        when (action) {
            is OnAppear -> fetchMiniatures(action.minisIds)
        }
    }

    private fun fetchMiniatures(minisIds: List<Long>) {
        viewModelScope.launch {
            minisRepository.getMiniatures(miniaturesId = minisIds)
                .catch {
                    //TODO: Manage error
                }
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