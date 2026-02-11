package com.devalr.painting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.gh.GHImage
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.rememberScreenSize
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun PaintingMiniCard(miniature: MiniatureBo) {
    Box(
        modifier = Modifier
            .size(calculateHeight(rememberScreenSize()))
            .clip(RoundedCornerShape(5.dp))
    ) {
        GHImage(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            borderRadius = 0.dp,
            imageUri = miniature.imageUri,
            size = calculateHeight(rememberScreenSize())
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .height(30.dp)
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GHText(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    text = miniature.name.capitalize(),
                    textAlign = TextAlign.Center,
                    type = TextType.LabelMBold
                )
            }
        }
    }
}

@Composable
private fun calculateHeight(screenSize: ScreenSize) = when (screenSize) {
    ScreenSize.COMPACT -> 150.dp
    ScreenSize.MEDIUM -> 250.dp
    else -> 300.dp
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