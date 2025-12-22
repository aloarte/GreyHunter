package com.devalr.domain.model

data class CompletionProportionsBo(
    val assembled: Float = 0.2f,
    val primed: Float = 0.2f,
    val baseColored: Float = 0.3f,
    val detailed:Float = 0.2f,
    val base: Float = 0.1f
)