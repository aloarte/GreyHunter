package com.devalr.painting.interactions

import com.devalr.domain.model.MiniatureBo


data class State(
    val minisLoaded: Boolean = false,
    val miniatures: List<MiniatureBo> = emptyList()

)