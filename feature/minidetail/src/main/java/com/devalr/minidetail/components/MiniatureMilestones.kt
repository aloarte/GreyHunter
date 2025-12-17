package com.devalr.minidetail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.enum.MilestoneType.Assembled
import com.devalr.domain.enum.MilestoneType.Base
import com.devalr.domain.enum.MilestoneType.BaseColored
import com.devalr.domain.enum.MilestoneType.Details
import com.devalr.domain.enum.MilestoneType.Primed
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.minidetail.R

@Composable
fun MiniatureMilestones(
    completion: MiniCompletionBo,
    onMilestone: (MilestoneType, Boolean) -> Unit
) {
    MiniatureMilestone(
        type = Assembled,
        milestoneAchieved = completion.isAssembled
    ) { type, enabled ->
        onMilestone(type, enabled)
    }
    MiniatureMilestone(type = Primed, milestoneAchieved = completion.isPrimed) { type, enabled ->
        onMilestone(type, enabled)
    }
    MiniatureMilestone(
        type = BaseColored,
        milestoneAchieved = completion.isBaseColored
    ) { type, enabled ->
        onMilestone(type, enabled)
    }
    MiniatureMilestone(type = Details, milestoneAchieved = completion.isDetailed) { type, enabled ->
        onMilestone(type, enabled)
    }
    MiniatureMilestone(
        type = Base,
        milestoneAchieved = completion.baseIsFinished
    ) { type, enabled ->
        onMilestone(type, enabled)
    }
}

@Composable
private fun MiniatureMilestone(
    type: MilestoneType,
    milestoneAchieved: Boolean,
    updateMilestone: (MilestoneType, Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        GHText(text = getMiniatureMilestoneText(type), type = TextType.LabelM)
        Checkbox(
            checked = milestoneAchieved,
            onCheckedChange = { updateMilestone(type, it) }
        )
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