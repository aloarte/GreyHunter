package com.devalr.home.model


sealed interface GamificationMessageType {
    object EmptyProjects : GamificationMessageType
    data class AlmostDone(val projectName: String) : GamificationMessageType
    data class ProgressRange(val progress: Float) : GamificationMessageType
    object None : GamificationMessageType
}