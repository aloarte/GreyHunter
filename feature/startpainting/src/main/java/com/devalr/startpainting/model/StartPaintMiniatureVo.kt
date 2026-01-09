package com.devalr.startpainting.model

data class StartPaintMiniatureVo(
    val id: Long,
    val projectId: Long,
    val name: String,
    val imageUri: String? = null,
    val isSelected: Boolean = false
)