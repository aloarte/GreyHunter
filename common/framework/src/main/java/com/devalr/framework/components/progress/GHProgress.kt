package com.devalr.framework.components.progress

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GHProgressBar(
    percentage: Float,
    modifier: Modifier = Modifier,
    spectrum: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green),
    height: Dp = 10.dp,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f)
) {
    val safePercentage = percentage.coerceIn(0f, 1f)
    val targetColor = calculateColorForPercentage(safePercentage, spectrum)

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500),
        label = "ColorAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(percent = 50))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = safePercentage)
                .fillMaxHeight()
                .clip(RoundedCornerShape(percent = 50))
                .background(animatedColor)
        )
    }
}




@Preview(showBackground = true)
@Composable
private fun ProgressBarPreviewCustomColors() {
    val spectrum = listOf(Color.Blue, Color.Cyan, Color.Magenta,Color.Black)
    Column(Modifier.padding(10.dp), verticalArrangement = spacedBy(1.dp)) {
        for (i in 0..10) {
            GHProgressBar(percentage = i / 10f, spectrum = spectrum)
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreviewDefaultColors() {
    Column(Modifier.padding(10.dp), verticalArrangement = spacedBy(1.dp)) {
        for (i in 0..10) {
            GHProgressBar(percentage = i / 10f)
        }
    }
}

