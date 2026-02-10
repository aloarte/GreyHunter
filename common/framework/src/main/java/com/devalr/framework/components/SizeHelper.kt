package com.devalr.framework.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

enum class ScreenSize {
    COMPACT,
    MEDIUM,
    EXPANDED }

@Composable
fun rememberScreenSize(): ScreenSize {
    val configuration = LocalConfiguration.current

    return remember(configuration.screenWidthDp) {
        when {
            configuration.screenWidthDp < 600 -> ScreenSize.COMPACT
            configuration.screenWidthDp < 840 -> ScreenSize.MEDIUM
            else -> ScreenSize.EXPANDED
        }
    }
}