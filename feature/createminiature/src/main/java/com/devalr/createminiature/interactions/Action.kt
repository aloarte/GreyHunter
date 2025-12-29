package com.devalr.createminiature.interactions

import android.net.Uri

sealed interface Action {
    data class OnAppear(val projectId: Long, val miniatureId: Long = -1) : Action
    data class OnNameChanged(val name: String) : Action
    data class OnImageChanged(val imageUri: Uri) : Action
    data object OnAddMiniature : Action
}