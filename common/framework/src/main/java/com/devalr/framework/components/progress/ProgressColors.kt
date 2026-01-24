package com.devalr.framework.components.progress

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

typealias ColorList = List<Color>

val LocalProgressColors = staticCompositionLocalOf<ColorList> { emptyList() }

object ProgressColorStore {
    var colors by mutableStateOf<ColorList>(emptyList())
        private set

    fun updateColors(newColors: ColorList) {
        colors = newColors
    }
}

@Composable
fun ProvideProgressColors(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalProgressColors provides ProgressColorStore.colors) {
        content()
    }
}