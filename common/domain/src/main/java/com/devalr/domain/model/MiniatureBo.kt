package com.devalr.domain.model

data class MiniatureBo(
    val id: Long = 0,
    val projectId: Long,
    val name: String,
    val imageUri: String? = null,
    val percentage: Float = 0f,
    val sortOrder: Int = 0,
    val completion: MiniCompletionBo = MiniCompletionBo()
)