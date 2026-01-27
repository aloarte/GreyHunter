package com.devalr.createminiature.interactions

import android.net.Uri

sealed interface Action {
    data class Load(val projectId: Long, val miniatureId: Long? = null) : Action
    data class ChangeName(val name: String) : Action
    data class ChangeImage(val imageUri: Uri) : Action
    data class RaiseError(val error: Exception, val errorType: ErrorType) : Action
    data object AddMiniature : Action
    data object Return : Action
}