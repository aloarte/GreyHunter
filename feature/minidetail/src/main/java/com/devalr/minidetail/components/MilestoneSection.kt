package com.devalr.minidetail.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.enums.MilestoneType
import com.devalr.domain.enums.MilestoneType.Assembled
import com.devalr.domain.enums.MilestoneType.Base
import com.devalr.domain.enums.MilestoneType.BaseColored
import com.devalr.domain.enums.MilestoneType.Details
import com.devalr.domain.enums.MilestoneType.Primed
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.minidetail.R

@Composable
fun MilestoneSection(
    type: MilestoneType,
    milestoneAchieved: Boolean,
    updateMilestone: (MilestoneType, Boolean) -> Unit
) {
    val transition =
        updateTransition(targetState = milestoneAchieved, label = "milestoneTransition")
    val animationDuration = 300
    val topWhiskerProgress by transition.animateFloat(
        transitionSpec = {
            val delay = if (targetState) 0 else animationDuration
            tween(durationMillis = animationDuration, delayMillis = delay)
        },
        label = "topWhisker"
    ) { isAchieved ->
        if (isAchieved) 1f else 0f
    }
    val primaryColor = MaterialTheme.colorScheme.primary
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Canvas(modifier = Modifier.fillMaxHeight()) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(x = center.x, y = 0f),
                    end = Offset(x = center.x, y = size.height),
                    strokeWidth = 40f
                )
                if (topWhiskerProgress > 0f) {
                    drawLine(
                        color = primaryColor,
                        start = Offset(x = center.x, y = 0f),
                        end = Offset(x = center.x, y = size.height * topWhiskerProgress),
                        strokeWidth = 40f
                    )
                }
            }

            GHText(text = getMiniatureMilestoneText(type), type = TextType.LabelM)
            Checkbox(
                checked = milestoneAchieved,
                onCheckedChange = { updateMilestone(type, it) }
            )
        }
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

@Preview(showBackground = true)
@Composable
private fun MilestoneSectionPreview() {
    GreyHunterTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.height(400.dp)) {
                MilestoneSection(
                    type = Assembled,
                    milestoneAchieved = false
                ) { _, _ ->
                    // Do nothing
                }
            }
            Box(modifier = Modifier.height(400.dp)) {
                MilestoneSection(
                    type = BaseColored,
                    milestoneAchieved = true
                ) { _, _ ->
                    // Do nothing
                }
            }
        }
    }
}
