package com.devalr.startpainting.interactions

import com.devalr.startpainting.model.StartPaintMiniatureVo

sealed interface Action {
    data object Load : Action
    data object Return : Action

    data object StartPainting : Action

    data class SelectMiniature(val miniature: StartPaintMiniatureVo) : Action
}