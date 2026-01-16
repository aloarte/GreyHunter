package com.devalr.minidetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.enum.MilestoneType.Assembled
import com.devalr.domain.enum.MilestoneType.Base
import com.devalr.domain.enum.MilestoneType.BaseColored
import com.devalr.domain.enum.MilestoneType.Details
import com.devalr.domain.enum.MilestoneType.Primed
import com.devalr.domain.model.CompletionProportionsBo
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.minidetail.R

@Composable
fun MiniatureMilestones(
    modifier: Modifier = Modifier,
    completion: MiniCompletionBo,
    proportions: CompletionProportionsBo,
    onMilestone: (MilestoneType, Boolean) -> Unit
) {
    val reviewedProportions =
        if (reviewProportion(proportions)) proportions else CompletionProportionsBo()
    val milestoneData = listOf(
        Assembled to reviewedProportions.assembled,
        Primed to reviewedProportions.primed,
        BaseColored to reviewedProportions.baseColored,
        Details to reviewedProportions.detailed,
        Base to reviewedProportions.base
    )

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp)
            .height(400.dp)
    ) {
        GHText(
            text = stringResource(R.string.label_milestones_description),
            lineHeight = 14.sp,
            type = TextType.LabelS
        )

        Spacer(modifier = Modifier.height(20.dp))

        milestoneData.forEach { (type, proportion) ->
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(proportion.coerceAtLeast(0.01f))
            ) {
                MilestoneSection(
                    type = type,
                    milestoneAchieved = when (type) {
                        Assembled -> completion.isAssembled
                        Primed -> completion.isPrimed
                        BaseColored -> completion.isBaseColored
                        Details -> completion.isDetailed
                        Base -> completion.baseIsFinished
                    },
                    updateMilestone = onMilestone
                )
            }
        }
    }
}

private fun reviewProportion(proportions: CompletionProportionsBo): Boolean {
    val total = proportions.assembled +
            proportions.primed +
            proportions.baseColored +
            proportions.detailed +
            proportions.base
    return total > 0.99f && total < 1.01f
}

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionAssembled() {
    GreyHunterTheme {
        MiniatureMilestones(
            proportions = CompletionProportionsBo(
                assembled = 0.4f,
                primed = 0.1f,
                baseColored = 0.05f,
                detailed = 0.3f,
                base = 0.05f
            ),
            completion = MiniCompletionBo(
                isAssembled = true,
                isPrimed = true,
                baseIsFinished = true
            )
        ) { _, _ ->
            //Do nothing
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionPrimed() {
    GreyHunterTheme {
        MiniatureMilestones(
            proportions = CompletionProportionsBo(
                assembled = 0.1f,
                primed = 0.6f,
                baseColored = 0.1f,
                detailed = 0.1f,
                base = 0.1f

            ),
            completion = MiniCompletionBo(
                isAssembled = true,
                isPrimed = true,
                isBaseColored = true,
                isDetailed = true,
                baseIsFinished = true
            )
        ) { _, _ ->
            //Do nothing
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionBaseColoured() {
    GreyHunterTheme {
        MiniatureMilestones(
            proportions = CompletionProportionsBo(
                assembled = 0.1f,
                primed = 0.2f,
                baseColored = 0.5f,
                detailed = 0.1f,
                base = 0.1f

            ),
            completion = MiniCompletionBo(
                isAssembled = true,
                isPrimed = true,
                baseIsFinished = true
            )
        ) { _, _ ->
            //Do nothing
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionDetail() {
    GreyHunterTheme {
        MiniatureMilestones(
            proportions = CompletionProportionsBo(
                assembled = 0.1f,
                primed = 0.2f,
                baseColored = 0.2f,
                detailed = 0.3f,
                base = 0.1f

            ),
            completion = MiniCompletionBo(
                isAssembled = true,
                isPrimed = true
            )
        ) { _, _ ->
            //Do nothing
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewWrongProportion() {
    GreyHunterTheme {
        MiniatureMilestones(
            proportions = CompletionProportionsBo(
                assembled = 10f,
                primed = 0.2f,
                baseColored = 0.2f,
                detailed = 0.3f,
                base = 0.1f

            ),
            completion = MiniCompletionBo(
                isAssembled = true,
                isPrimed = true,
                isBaseColored = true
            )
        ) { _, _ ->
            //Do nothing
        }
    }
}