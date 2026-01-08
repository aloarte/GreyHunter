package com.devalr.framework.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class ScreenSize { SMALL, MEDIUM, LARGE }

@Composable
fun getScreenSize(): ScreenSize {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    return when {
        screenWidthDp < 400 -> ScreenSize.SMALL
        screenWidthDp < 430 -> ScreenSize.MEDIUM
        else -> ScreenSize.LARGE
    }
}