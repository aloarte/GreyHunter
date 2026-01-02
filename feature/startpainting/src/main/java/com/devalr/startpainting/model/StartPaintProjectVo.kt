package com.devalr.startpainting.model

data class StartPaintProjectVo(
    val id: Long,
    val name: String,
    val minis: List<StartPaintMiniatureVo>
)

