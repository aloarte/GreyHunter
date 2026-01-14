package com.devalr.painting.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.InfiniteHorizontalCarousel
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun PaintingMinisCarousel(miniatures: List<MiniatureBo>) {
    InfiniteHorizontalCarousel(
        modifier = Modifier.padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(200.dp),
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        items = miniatures,
        userScrollEnabled = false
    ) { item ->
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PaintingMiniCard(miniature = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingMinisCarouselLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        PaintingMinisCarousel(
            miniatures = listOf(
                technomancer,
                chronomancer
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingMinisCarouselDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        PaintingMinisCarousel(
            miniatures = listOf(
                technomancer,
                chronomancer
            )
        )
    }
}