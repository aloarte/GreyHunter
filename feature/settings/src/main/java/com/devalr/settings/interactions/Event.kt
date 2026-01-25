package com.devalr.settings.interactions

import com.devalr.framework.components.snackbar.SnackBarType

sealed interface Event {
    data object NavigateBack : Event
    data class LaunchSnackBar(val import:Boolean, val type: SnackBarType) : Event

}