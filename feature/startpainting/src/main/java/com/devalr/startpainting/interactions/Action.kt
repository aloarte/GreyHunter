package com.devalr.startpainting.interactions

import com.devalr.startpainting.model.StartPaintMiniatureVo

sealed interface Action {
    data object OnAppear : Action
    data object OnBackPressed : Action

    data object OnStartPainting : Action

    data class OnSelectMiniature(val miniature: StartPaintMiniatureVo) : Action
}