package com.devalr.settings.interactions

import android.net.Uri
import com.devalr.domain.enums.ProgressColorType
import com.devalr.domain.enums.ThemeType

sealed interface Action {
    data object Load : Action
    data object Return : Action
    data class ChangeAppearance(val mode: ThemeType) : Action
    data class ChangeProgressColors(val colorType: ProgressColorType) : Action
    data class ExportProjects(val uri: Uri) : Action
    data class ImportProjects(val uri: Uri) : Action

}