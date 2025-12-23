package com.devalr.minidetail.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.model.CompletionProportionsBo
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.minidetail.R


@Composable
fun MiniatureDetailScreenContent(
    miniature: MiniatureBo,
    onMilestone: (MilestoneType, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item { GHText(text = miniature.name, type = TextType.Featured) }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item {
            GHText(
                text = getMiniatureProgressMessage(progress = miniature.percentage),
                type = TextType.Description,
                italic = true
            )
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item { GHImage(imageUri = miniature.imageUri, size = 160.dp) }

        item { Spacer(modifier = Modifier.height(20.dp)) }
        item {
            GHText(
                text = stringResource(R.string.label_milestones),
                lineHeight = 14.sp,
                type = TextType.LabelS
            )
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item {
            MiniatureMilestones(
                completion = miniature.completion,
                proportions = CompletionProportionsBo(
                    assembled = 0.4f,
                    primed = 0.1f,
                    baseColored = 0.1f,
                    detailed = 0.3f,
                    base = 0.1f
                )
            ) { type, enabled ->
                onMilestone(type, enabled)
            }
        }
    }
}

@Composable
private fun getMiniatureProgressMessage(progress: Float): String {
    val res = when (progress) {
        0f -> R.string.label_miniature_progress_gamification_0
        in 0.01f..0.50f -> R.string.label_miniature_progress_gamification_25
        in 0.50f..0.75f -> R.string.label_miniature_progress_gamification_50
        in 0.75f..0.99f -> R.string.label_miniature_progress_gamification_75
        1.0f -> R.string.label_miniature_progress_gamification_100
        else -> R.string.label_miniature_progress_gamification_error
    }
    return stringResource(res)
}

@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        MiniatureDetailScreenContent(
            miniature = MiniatureBo(
                name = "",
                completion = MiniCompletionBo(isAssembled = true, isPrimed = true),
                projectId = 1L,
                percentage = 1f
            )
        ) { _, _ ->
            // Do nothing
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun MiniatureDetailScreenContentPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        MiniatureDetailScreenContent(
            miniature = MiniatureBo(
                name = "",
                completion = MiniCompletionBo(isAssembled = true, isPrimed = true),
                projectId = 1L,
                percentage = 0.5f
            )
        ) { _, _ ->
            // Do nothing
        }
    }

}