package com.devalr.startpainting.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.helpers.chronomancerVo
import com.devalr.startpainting.model.helpers.technomancerVo

@Composable
fun StartPaintingMiniatureCard(
    modifier: Modifier = Modifier,
    miniature: StartPaintMiniatureVo,
    size: Dp = 125.dp,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(key1 = miniature.isSelected) {
        if (miniature.isSelected) {
            scale.animateTo(
                targetValue = 0.95f,
                animationSpec = tween(durationMillis = 300)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )
        }
    }

    Box(
        modifier = modifier
            .scale(scale.value)
            .size(size)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onMiniatureSelected(miniature) }) {

        GHImage(
            modifier = Modifier.fillMaxSize(),
            borderRadius = 0.dp,
            imageUri = miniature.imageUri,
            size = size
        )

        if (miniature.isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(30.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            GHText(
                text = miniature.name,
                type = TextType.LabelMBold,
                textColor = Color.White
            )

        }

        if (miniature.isSelected) {
            Icon(
                modifier = Modifier
                    .padding(5.dp)
                    .size(20.dp)
                    .align(Alignment.TopEnd),
                imageVector = Icons.Default.Check,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = ""
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintingMiniatureCardLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StartPaintingMiniatureCard(
                miniature = chronomancerVo.copy(isSelected = true),
                onMiniatureSelected = {
                    //Do nothing
                }
            )
            StartPaintingMiniatureCard(
                miniature = technomancerVo.copy(isSelected = false),
                onMiniatureSelected = {
                    //Do nothing
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun StartPaintingMiniatureCardFalseModePreview() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StartPaintingMiniatureCard(
                miniature = chronomancerVo.copy(isSelected = true),
                onMiniatureSelected = {
                    //Do nothing
                }
            )
            StartPaintingMiniatureCard(
                miniature = technomancerVo.copy(isSelected = false),
                onMiniatureSelected = {
                    //Do nothing
                }
            )
        }
    }
}