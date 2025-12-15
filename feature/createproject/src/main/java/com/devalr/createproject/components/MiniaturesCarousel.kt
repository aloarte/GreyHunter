package com.devalr.createproject.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.devalr.createproject.components.cards.MiniatureCard
import com.devalr.createproject.model.MiniatureVo
import com.devalr.createproject.model.MiniatureVo.AddMiniature
import com.devalr.createproject.model.MiniatureVo.MiniatureItem
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.framework.components.cards.AddCard
import com.devalr.framework.enum.CardType

@Composable
fun MiniaturesCarousel(
    projects: List<MiniatureVo>,
    onMiniatureClicked: (Long) -> Unit,
    onCreateMiniature: () -> Unit
) {
    HorizontalCarousel(items = projects) { item ->
        when (item) {
            is MiniatureItem -> MiniatureCard(miniatureBo = item.miniature)

            is AddMiniature -> AddCard(
                type = CardType.Miniature,
                onCreate = onCreateMiniature
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun MiniaturesCarouselPreview() {
    MiniaturesCarousel(
        projects = listOf(
            MiniatureItem(chronomancer),
            AddMiniature
        ),
        onMiniatureClicked = {
            // Do nothing
        },
        onCreateMiniature = {
            // Do nothing
        }
    )

}
