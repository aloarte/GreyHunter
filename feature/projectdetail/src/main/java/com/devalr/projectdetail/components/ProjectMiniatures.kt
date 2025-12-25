package com.devalr.projectdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.components.progress.VerticalProgress


@Composable
fun ProjectMiniatures(
    miniatures: List<MiniatureBo>,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GHButton(text = "+", onClick = onCreateMiniature)
        miniatures.forEach { miniature ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .height(60.dp)
                    .clickable { onNavigateToMiniature(miniature.id) }
            ) {
                Row(modifier = Modifier.align(Alignment.CenterStart)) {
                    VerticalProgress(progress = miniature.percentage)
                    Spacer(modifier = Modifier.width(5.dp))
                    miniature.imageUri?.let {
                        GHImage(
                            modifier = Modifier.padding(2.dp),
                            imageUri = it,
                            size = 60.dp,
                            borderRadius = 6.dp
                        )
                    }
                }
                GHText(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    text = miniature.name,
                    type = TextType.LabelM
                )
                GHText(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    text = "${miniature.percentage}% completed",
                    type = TextType.LabelM
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProjectMiniaturesPreview() {
    ProjectMiniatures(
        miniatures = listOf(MiniatureBo(name = "Necron", projectId = 1)),
        onCreateMiniature = {
            // Do nothing
        },
        onNavigateToMiniature = {
            // Do nothing
        }
    )
}