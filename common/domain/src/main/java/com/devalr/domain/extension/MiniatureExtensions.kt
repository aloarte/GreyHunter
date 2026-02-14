package com.devalr.domain.extension

import com.devalr.domain.enums.MilestoneType
import com.devalr.domain.enums.MilestoneType.Assembled
import com.devalr.domain.enums.MilestoneType.Base
import com.devalr.domain.enums.MilestoneType.BaseColored
import com.devalr.domain.enums.MilestoneType.Details
import com.devalr.domain.enums.MilestoneType.Primed
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo

fun MiniCompletionBo.toggle(type: MilestoneType): MiniCompletionBo {
    return when (type) {
        Assembled -> copy(isAssembled = !isAssembled)
        Primed -> copy(isPrimed = !isPrimed)
        BaseColored -> copy(isBaseColored = !isBaseColored)
        Details -> copy(isDetailed = !isDetailed)
        Base -> copy(baseIsFinished = !baseIsFinished)
    }
}

fun MiniatureBo.recalculateProgress(): MiniatureBo {
    val newPercentage = MilestoneType.entries.sumOf { milestoneType ->
        val isAchieved = when (milestoneType) {
            Assembled -> completion.isAssembled
            Primed -> completion.isPrimed
            BaseColored -> completion.isBaseColored
            Details -> completion.isDetailed
            Base -> completion.baseIsFinished
        }
        if (isAchieved) milestoneType.getWeight().toDouble() else 0.0
    }.toFloat()
    return this.copy(percentage = newPercentage)
}


fun MiniCompletionBo.isMilestoneAchievable(
    type: MilestoneType,
    enable: Boolean
): Boolean = if (enable) {
    when (type) {
        Assembled -> true
        Primed -> isAssembled
        BaseColored -> isAssembled && isPrimed
        Details -> isAssembled && isPrimed && isBaseColored
        Base -> true
    }
} else {
    when (type) {
        Assembled -> !isPrimed && !isBaseColored && !isDetailed
        Primed -> !isBaseColored && !isDetailed
        BaseColored -> !isDetailed
        Details -> true
        Base -> true
    }
}


private fun MilestoneType.getWeight(): Float {
    return when (this) {
        Assembled -> 0.2f
        Primed -> 0.2f
        BaseColored -> 0.3f
        Details -> 0.2f
        Base -> 0.1f
    }
}
