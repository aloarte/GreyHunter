package com.devalr.framework.components.progress

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun calculateColorForPercentage(percentage: Float, spectrum: List<Color>): Color {
    if (spectrum.isEmpty()) return Color.Gray
    if (spectrum.size == 1) return spectrum.first()
    val scaledValue = percentage * (spectrum.size - 1)
    val firstSpectrumColorIndex = scaledValue.toInt()
    val secondSpectrumColorIndex = (firstSpectrumColorIndex + 1).coerceAtMost(spectrum.size - 1)
    val fraction = scaledValue - firstSpectrumColorIndex

    return lerp(spectrum[firstSpectrumColorIndex], spectrum[secondSpectrumColorIndex], fraction)
}