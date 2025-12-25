package com.devalr.minidetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.model.CompletionProportionsBo
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.GHIconButton
import com.devalr.framework.components.GHImage
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun MiniatureDetailScreenContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    miniature: MiniatureBo,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onMilestone: (MilestoneType, Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {

        GHImage(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            borderRadius = 0.dp,
            imageUri = miniature.imageUri,
            size = 160.dp
        )

        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            onBackPressed = onBackPressed,
            onEditPressed = onEditPressed
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {
            item { Spacer(modifier = Modifier.height(200.dp)) }

            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .clip(
                            RoundedCornerShape(
                                topStart = 24.dp,
                                topEnd = 24.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp)
                ) {
                    LazyColumn {
                        item { MiniatureInfo(miniature) }
                        item {
                            MiniatureMilestones(
                                completion = miniature.completion,
                                proportions = CompletionProportionsBo(
                                    assembled = 0.2f,
                                    primed = 0.2f,
                                    baseColored = 0.3f,
                                    detailed = 0.2f,
                                    base = 0.1f
                                )
                            ) { type, enabled ->
                                onMilestone(type, enabled)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopButtons(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        GHIconButton(icon = Icons.Default.ArrowBack, onButtonClicked = onBackPressed)
        Spacer(modifier = Modifier.width(10.dp))
        GHIconButton(icon = Icons.Default.Edit, onButtonClicked = onEditPressed)

    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        MiniatureDetailScreenContent(
            miniature = MiniatureBo(
                name = "Mini name",
                completion = MiniCompletionBo(isAssembled = true, isPrimed = true),
                projectId = 1L,
                percentage = 1f
            ),
            onBackPressed = {
                // Do nothing
            },
            onMilestone = { _, _ ->
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        MiniatureDetailScreenContent(
            miniature = MiniatureBo(
                name = "Mini name",
                completion = MiniCompletionBo(isAssembled = true, isPrimed = true),
                projectId = 1L,
                percentage = 0.5f
            ),
            onBackPressed = {
                // Do nothing
            },
            onMilestone = { _, _ ->
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            }
        )
    }
}