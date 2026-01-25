package com.devalr.framework.components.snackbar

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals


enum class SnackBarType {
    SUCCESS, ERROR
}

data class SnackBarVisualsCustom(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false,
    val type: SnackBarType = SnackBarType.SUCCESS
) : SnackbarVisuals

data class GHSnackBarData(
    override val visuals: SnackBarVisualsCustom
) : SnackbarData {
    override fun dismiss() {}
    override fun performAction() {}
}