package com.devalr.startpainting.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.devalr.framework.components.GHText
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.framework.components.TextType
import com.devalr.framework.components.cards.getCardWidth
import com.devalr.framework.enum.CardType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import com.devalr.startpainting.model.helpers.hierotekCircleProjectVo
import com.devalr.startpainting.model.helpers.stormlightArchiveProjectVo

@Composable
fun StartPantingProjectsCarousel(
    projects: List<StartPaintProjectVo>,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {
    HorizontalCarousel(
        items = projects,
        height = 500.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) { item ->
        StartPaintingProjectCard(projectBo = item, onMiniatureSelected = onMiniatureSelected)

    }
}

@Composable
fun StartPaintingProjectCard(
    modifier: Modifier = Modifier,
    projectBo: StartPaintProjectVo,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {
    Card(
        modifier = modifier
            .width(getCardWidth(CardType.StartPaintProject))
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            GHText(text = projectBo.name, type = TextType.Title)
            if (projectBo.minis.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    GHText(text = "No hay miniaturas en este proyecto", type = TextType.Description)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(projectBo.minis) { miniature ->
                        StartPaintingMiniatureCard(
                            miniature = miniature,
                            onMiniatureSelected = onMiniatureSelected
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        StartPantingProjectsCarousel(
            projects = listOf(
                hierotekCircleProjectVo,
                stormlightArchiveProjectVo,
            ),
            onMiniatureSelected = {
                //Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        StartPantingProjectsCarousel(
            projects = listOf(
                hierotekCircleProjectVo,
                stormlightArchiveProjectVo,
            ),
            onMiniatureSelected = {
                //Do nothing
            }
        )
    }
}