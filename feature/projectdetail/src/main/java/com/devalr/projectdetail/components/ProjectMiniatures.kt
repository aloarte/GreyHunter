package com.devalr.projectdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.DETAIL_PROJECT_CREATE_MINI_FAB
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.projectdetail.model.SortDirection

@Composable
fun ProjectMiniatures(
    miniatures: List<MiniatureBo>,
    animatedMiniIds: Pair<Long, Long>? = null,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit,
    onSortMiniature: (SortDirection, Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        miniatures.forEach { miniature ->
            MiniatureCardRow(
                miniature = miniature,
                upDisabled = miniatures.indexOf(miniature) == 0,
                downDisabled = miniatures.indexOf(miniature) == miniatures.size - 1,
                animate = animatedMiniIds?.first == miniature.id || animatedMiniIds?.second == miniature.id,
                onNavigateToMiniature = onNavigateToMiniature,
                onSortMiniature = onSortMiniature
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .semantics {
                    contentDescription = DETAIL_PROJECT_CREATE_MINI_FAB
                }) {
            FloatingActionButton(
                modifier = Modifier
                    .testTag(DETAIL_PROJECT_CREATE_MINI_FAB)
                    .semantics {
                        contentDescription = DETAIL_PROJECT_CREATE_MINI_FAB
                    },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onCreateMiniature

            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
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
                },
                onSortMiniature = { _, _ ->
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
                },
                onSortMiniature = { _, _ ->
                    // Do nothing
                }
            )
        }
    }
}