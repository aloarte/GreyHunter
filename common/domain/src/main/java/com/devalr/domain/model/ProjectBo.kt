package com.devalr.domain.model

data class ProjectBo(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val imageUri: String? = null,
    val progress: Float = 0f,
    val minis: List<MiniatureBo> = emptyList()
)