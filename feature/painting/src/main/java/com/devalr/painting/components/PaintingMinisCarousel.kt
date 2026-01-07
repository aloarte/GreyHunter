package com.devalr.painting.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.InfiniteHorizontalCarousel
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun PaintingMinisCarousel(miniatures: List<MiniatureBo>) {
    InfiniteHorizontalCarousel(items = miniatures) { item ->
        PaintingMiniCard(miniature = item)
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