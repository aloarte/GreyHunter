package com.devalr.startpainting.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import com.devalr.startpainting.model.helpers.hierotekCircleProjectVo

@Composable
fun StartPaintingProjectCard(
    modifier: Modifier = Modifier,
    projectBo: StartPaintProjectVo,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {
    Card(
        modifier = modifier
            .width(500.dp)
            .fillMaxHeight()
            .padding(horizontal = 40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 20.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            GHText(text = projectBo.name, type = TextType.Title)
            Spacer(modifier = Modifier.height(20.dp))
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

@Preview(showBackground = true)
@Composable
private fun StartPaintingProjectCardLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        StartPaintingProjectCard(
            projectBo = hierotekCircleProjectVo,
            onMiniatureSelected = {
                //Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintingProjectCardDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        StartPaintingProjectCard(
            projectBo = hierotekCircleProjectVo,
            onMiniatureSelected = {
                //Do nothing
            }
        )
    }
}