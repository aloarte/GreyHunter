package com.devalr.createproject.model

import com.devalr.domain.model.MiniatureBo

sealed interface MiniatureVo {
    data class MiniatureItem(val miniature: MiniatureBo) : MiniatureVo
    data object AddMiniature : MiniatureVo
}
