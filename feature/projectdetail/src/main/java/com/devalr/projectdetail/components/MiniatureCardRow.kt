package com.devalr.projectdetail.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.gh.GHImage
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.progress.GHCircularProgress
import com.devalr.framework.components.progress.LocalProgressColors
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.projectdetail.model.SortDirection
import com.devalr.projectdetail.model.SortDirection.Down
import com.devalr.projectdetail.model.SortDirection.Up

@Composable
fun MiniatureCardRow(
    miniature: MiniatureBo,
    upDisabled: Boolean,
    downDisabled: Boolean,
    animate: Boolean = false,
    onNavigateToMiniature: (Long) -> Unit,
    onSortMiniature: (SortDirection, Long) -> Unit
) {
    val scale = remember { Animatable(1f) }
    val elevation = remember { Animatable(1f) }

    LaunchedEffect(animate) {
        if (animate) {
            scale.animateTo(targetValue = 1.03f, animationSpec = tween(150))
            elevation.animateTo(targetValue = 8f, animationSpec = tween(150))
            scale.animateTo(targetValue = 1f, animationSpec = tween(150))
            elevation.animateTo(targetValue = 1f, animationSpec = tween(150))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                },
            elevation = CardDefaults.cardElevation(elevation.value.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = { onNavigateToMiniature(miniature.id) }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(end = 20.dp)
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GHImage(
                    modifier = Modifier,
                    imageUri = miniature.imageUri,
                    size = 80.dp,
                    borderRadius = 0.dp
                )
                GHText(
                    modifier = Modifier.fillMaxWidth(.5f),
                    text = miniature.name.capitalize(),
                    type = TextType.LabelL,
                    singleLane = true
                )
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GHText(
                        text = "${(miniature.percentage * 100).toInt()}%",
                        type = TextType.LabelS,
                        singleLane = true
                    )

                    GHCircularProgress(
                        percentage = miniature.percentage,
                        spectrum = LocalProgressColors.current
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(60.dp)
                .offset(x = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(vertical = 1.dp, horizontal = 1.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            IconButton(
                enabled = !upDisabled,
                onClick = { onSortMiniature(Up, miniature.id) },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.ArrowCircleUp,
                    contentDescription = "Move up"
                )
            }

            IconButton(
                enabled = !downDisabled,
                onClick = { onSortMiniature(Down, miniature.id) },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.ArrowCircleDown,
                    contentDescription = "Move down"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureCardRowLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(20.dp)) {
            MiniatureCardRow(
                miniature = MiniatureBo(name = "Necron 1", projectId = 1, percentage = 1.0f),
                upDisabled = true,
                downDisabled = false,
                onSortMiniature = { _, _ ->
                    // Do nothing
                },
                onNavigateToMiniature = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureCardRowDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(20.dp)) {
            MiniatureCardRow(
                miniature = MiniatureBo(name = "Necron 1", projectId = 1, percentage = 1.0f),
                upDisabled = false,
                downDisabled = true,
                onSortMiniature = { _, _ ->
                    // Do nothing
                },
                onNavigateToMiniature = {
                    // Do nothing
                }
            )
        }
    }
}