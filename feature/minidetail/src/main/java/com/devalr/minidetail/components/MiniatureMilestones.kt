package com.devalr.minidetail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.enum.MilestoneType.Assembled
import com.devalr.domain.enum.MilestoneType.Base
import com.devalr.domain.enum.MilestoneType.BaseColored
import com.devalr.domain.enum.MilestoneType.Details
import com.devalr.domain.enum.MilestoneType.Primed
import com.devalr.domain.model.CompletionProportionsBo
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.minidetail.R

private fun reviewProportion(proportions: CompletionProportionsBo): Boolean {
    val total =
        proportions.assembled + proportions.primed + proportions.baseColored + proportions.detailed + proportions.base
    return total > 0.99f && total < 1.01f
}


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
            .fillMaxWidth()
            .height(400.dp)
    ) {
        milestoneData.forEach { (type, proportion) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(proportion.coerceAtLeast(0.01f))
            ) {
                MilestoneSection(
                    type = type,
                    proportion = proportion,
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

@Composable
private fun MilestoneSection(
    type: MilestoneType,
    proportion: Float,
    milestoneAchieved: Boolean,
    updateMilestone: (MilestoneType, Boolean) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (milestone, topLine, bottomLine, percentageText) = createRefs()

        MiniatureMilestone(
            modifier = Modifier.constrainAs(milestone) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            type = type,
            milestoneAchieved = milestoneAchieved,
            updateMilestone = updateMilestone
        )

        val percentageModifier = Modifier.constrainAs(percentageText) {
            end.linkTo(parent.end, margin = 16.dp)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        GHText(
            text = "${(proportion * 100).toInt()}%",
            type = TextType.Description,
            modifier = percentageModifier,
            textAlign = TextAlign.Center
        )

        Canvas(
            modifier = Modifier.constrainAs(topLine) {
                end.linkTo(percentageText.end, margin = 4.dp)
                start.linkTo(percentageText.start, margin = 4.dp)
                top.linkTo(parent.top)
                bottom.linkTo(percentageText.top, margin = 4.dp)
                height = fillToConstraints
            }
        ) {
            drawLine(
                color = if (milestoneAchieved) Color.Green else Color.Gray,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = size.height),
                strokeWidth = if (milestoneAchieved) 5f else 3f
            )
        }

        Canvas(
            modifier = Modifier.constrainAs(bottomLine) {
                end.linkTo(percentageText.end, margin = 4.dp)
                start.linkTo(percentageText.start, margin = 4.dp)
                top.linkTo(percentageText.bottom, margin = 4.dp)
                bottom.linkTo(parent.bottom)
                height = fillToConstraints
            }
        ) {
            drawLine(
                color = if (milestoneAchieved) Color.Green else Color.Gray,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = size.height),
                strokeWidth = if (milestoneAchieved) 5f else 3f
            )
        }
    }
}

@Composable
private fun MiniatureMilestone(
    modifier: Modifier = Modifier,
    type: MilestoneType,
    milestoneAchieved: Boolean,
    updateMilestone: (MilestoneType, Boolean) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = milestoneAchieved,
            onCheckedChange = { updateMilestone(type, it) }
        )
        GHText(text = getMiniatureMilestoneText(type), type = TextType.LabelM)
    }
}

@Composable
private fun getMiniatureMilestoneText(type: MilestoneType) = when (type) {
    Assembled -> stringResource(R.string.label_miniature_assembled)
    Primed -> stringResource(R.string.label_miniature_primed)
    BaseColored -> stringResource(R.string.label_miniature_base_colored)
    Details -> stringResource(R.string.label_miniature_details)
    Base -> stringResource(R.string.label_miniature_base)
}

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionAssembled() {
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

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionPrimed() {
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

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionBaseColoured() {
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

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewProportionDetail() {
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

@Composable
@Preview(showBackground = true)
private fun MiniatureMilestonesPreviewWrongProportion() {
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