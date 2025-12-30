package com.devalr.projectdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun ProjectMiniatures(
    miniatures: List<MiniatureBo>,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        miniatures.forEach { miniature ->
            Card(
                modifier = Modifier,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(5.dp),
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
                        borderRadius = 6.dp
                    )
                    GHText(
                        modifier = Modifier.fillMaxWidth(.5f),
                        text = miniature.name,
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
                        CircularProgressIndicator(
                            progress = { miniature.percentage },
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeWidth = 3.dp
                        )

                    }

                }
            }
        }
        FloatingActionButton(
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = onCreateMiniature

        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectMiniaturesLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(20.dp)) {
            ProjectMiniatures(
                miniatures = listOf(
                    MiniatureBo(name = "Necron 1", projectId = 1, percentage = 1.0f),
                    MiniatureBo(name = "Necron 2", projectId = 1, percentage = 0.5f),
                    MiniatureBo(name = "Necron 3", projectId = 1),
                    MiniatureBo(name = "Necron 4", projectId = 1)
                ),
                onCreateMiniature = {
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
private fun ProjectMiniaturesDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(20.dp)) {
            ProjectMiniatures(
                miniatures = listOf(
                    MiniatureBo(name = "Necron 1", projectId = 1, percentage = 1.0f),
                    MiniatureBo(name = "Necron 2", projectId = 1, percentage = 0.5f),
                    MiniatureBo(name = "Necron 3", projectId = 1),
                    MiniatureBo(name = "Necron 4", projectId = 1)
                ),
                onCreateMiniature = {
                    // Do nothing
                },
                onNavigateToMiniature = {
                    // Do nothing
                }
            )
        }
    }
}