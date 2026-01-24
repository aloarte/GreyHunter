package com.devalr.settings.interactions

import android.net.Uri
import com.devalr.domain.enum.ProgressColorType
import com.devalr.domain.enum.ThemeType

sealed interface Action {
    data object OnAppear : Action
    data object OnBackPressed : Action
    data class OnChangeAppearance(val mode: ThemeType) : Action
    data class OnChangeProgressColors(val colorType: ProgressColorType) : Action
    data class OnExportPressed(val uri: Uri) : Action
    data class OnImportPressed(val uri: Uri) : Action

}