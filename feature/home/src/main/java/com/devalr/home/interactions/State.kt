package com.devalr.home.interactions

import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.home.model.GamificationMessageType
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.ProjectVo

enum class ErrorType {
    RetrievingDatabase
}

data class State(
    val loaded: Boolean = false,
    val error: Boolean = false,
    val projects: List<ProjectVo> = emptyList(),
    val almostDoneProjects: List<ProjectBo> = emptyList(),
    val lastUpdatedMinis: List<MiniatureBo> = emptyList(),
    val gamificationSentence: GamificationMessageType = None
)