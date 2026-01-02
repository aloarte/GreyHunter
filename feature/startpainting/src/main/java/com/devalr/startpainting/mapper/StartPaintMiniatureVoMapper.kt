package com.devalr.startpainting.mapper

import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.startpainting.model.StartPaintMiniatureVo

class StartPaintMiniatureVoMapper() : Mapper<MiniatureBo, StartPaintMiniatureVo>() {
    override fun transform(data: MiniatureBo): StartPaintMiniatureVo = StartPaintMiniatureVo(
        id = data.id,
        projectId = data.projectId,
        name = data.name,
        imageUri = data.imageUri
    )

    override fun transformReverse(data: StartPaintMiniatureVo): MiniatureBo = MiniatureBo(
        id = data.id,
        projectId = data.projectId,
        name = data.name,
        imageUri = data.imageUri
    )
}
