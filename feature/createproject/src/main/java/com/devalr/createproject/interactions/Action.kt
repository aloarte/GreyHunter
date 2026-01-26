package com.devalr.createproject.interactions

import android.net.Uri

sealed interface Action {
    data class Load(val projectId: Long? = null) : Action
    data class ChangeName(val name: String) : Action
    data class ChangeDescription(val description: String) : Action
    data class ChangeImage(val imageUri: Uri) : Action
    data object AddProject : Action
}