package com.devalr.startpainting.interactions

import com.devalr.domain.model.MiniatureBo

sealed interface Event {
    data object NavigateBack : Event
    data class NavigatePaintMiniatures(val miniatures: List<MiniatureBo>) : Event
}