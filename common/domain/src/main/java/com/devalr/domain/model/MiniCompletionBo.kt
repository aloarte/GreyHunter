package com.devalr.domain.model

data class MiniCompletionBo(
    val isAssembled: Boolean = false,
    val isPrimed: Boolean = false,
    val isBaseColored: Boolean = false,
    val isDetailed: Boolean = false,
    val baseIsFinished: Boolean = false
)
