package com.devalr.painting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme


@Composable
fun PaintingScreenContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    miniatures: List<MiniatureBo>,
    onBackPressed: () -> Unit
) {
    PreloadMiniatureImages(miniatures)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(innerPadding)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        GHText(text = "Painting ${miniatures.size} miniatures", type = TextType.Featured)
        Spacer(modifier = Modifier.height(40.dp))
        GHText(
            text = "Take your time and paint the miniatures. Enjoy the journey.",
            type = TextType.Description
        )

        PaintingMinisCarousel(miniatures = miniatures)
        Spacer(modifier = Modifier.height(10.dp))
        GHButton(text = "Done painting", invertColors = true) {
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingScreenContentLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        PaintingScreenContent(
            miniatures = listOf(
                technomancer,
                chronomancer
            ),
            onBackPressed = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingScreenContentDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        PaintingScreenContent(
            miniatures = listOf(
                technomancer,
                chronomancer
            ),
            onBackPressed = {
                // Do nothing
            }
        )
    }
}