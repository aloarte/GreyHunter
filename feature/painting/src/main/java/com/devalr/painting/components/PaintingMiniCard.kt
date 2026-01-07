package com.devalr.painting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun PaintingMiniCard(miniature: MiniatureBo) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GHText(text = miniature.name, type = TextType.Title)
        GHImage(
            imageUri = miniature.imageUri,
            size = 280.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingMiniCardLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        PaintingMiniCard(miniature = technomancer)
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingMiniCardDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        PaintingMiniCard(miniature = technomancer)
    }
}

