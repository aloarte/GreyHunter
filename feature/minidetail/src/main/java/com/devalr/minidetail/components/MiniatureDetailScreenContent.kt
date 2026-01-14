package com.devalr.minidetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.model.CompletionProportionsBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.detail.TopButtons
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.minidetail.R

@Composable
fun MiniatureDetailScreenContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onlyUpdate: Boolean,
    miniature: MiniatureBo,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onDeletePressed: () -> Unit,
    onMilestone: (MilestoneType, Boolean) -> Unit
) {
    val saveAndContinueLabel = stringResource(R.string.label_save_continue)
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {

        GHImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            borderRadius = 0.dp,
            showMessage = true,
            imageUri = miniature.imageUri,
            size = 160.dp
        )
        if (onlyUpdate.not()) {
            TopButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                onBackPressed = onBackPressed,
                onEditPressed = onEditPressed,
                onDeletePressed = onDeletePressed
            )
        }

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
                    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                        item { MiniatureInfo(miniature = miniature, onlyUpdate = onlyUpdate) }
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
                        if (onlyUpdate) {
                            item {
                                GHButton(text = saveAndContinueLabel) {
                                    onBackPressed()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        MiniatureDetailScreenContent(
            miniature = chronomancer,
            onlyUpdate = false,
            onBackPressed = {
                // Do nothing
            },
            onMilestone = { _, _ ->
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            },
            onDeletePressed = {
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
            miniature = chronomancer,
            onlyUpdate = false,
            onBackPressed = {
                // Do nothing
            },
            onMilestone = { _, _ ->
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            },
            onDeletePressed = {
                // Do nothing
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewOnlyUpdateLightMode() {
    GreyHunterTheme(darkTheme = false) {
        MiniatureDetailScreenContent(
            miniature = chronomancer,
            onlyUpdate = true,
            onBackPressed = {
                // Do nothing
            },
            onMilestone = { _, _ ->
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            },
            onDeletePressed = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewOnlyUpdateDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        MiniatureDetailScreenContent(
            miniature = chronomancer,
            onlyUpdate = true,
            onBackPressed = {
                // Do nothing
            },
            onMilestone = { _, _ ->
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            },
            onDeletePressed = {
                // Do nothing
            }
        )
    }
}