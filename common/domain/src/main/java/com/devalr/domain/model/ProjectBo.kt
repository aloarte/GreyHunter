package com.devalr.domain.model

data class ProjectBo(
    val id: Long = 0,
    val name: String,
    val imageUri: String? = null,
    val percentage: Float = 0f,
    val minis: List<MiniatureBo> = emptyList()
)