package com.devalr.framework.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, ACTION, EVENT>(
    initialState: STATE
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    protected fun updateState(reducer: STATE.() -> STATE) {
        _uiState.value = _uiState.value.reducer()
    }

    private val _events = MutableSharedFlow<EVENT>()
    val events = _events.asSharedFlow()

    protected fun sendEvent(event: EVENT) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    abstract fun onAction(action: ACTION)
}
