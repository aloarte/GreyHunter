package com.devalr.createproject.interactions

import android.net.Uri

sealed interface Action {
    data object OnAppear : Action
    data class OnNameChanged(val name: String) : Action
    data class OnDescriptionChanged(val description: String) : Action
    data class OnImageChanged(val imageUri: Uri) : Action
    data object OnAddProject : Action
}