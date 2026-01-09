package com.devalr.startpainting.mapper

import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo

class StartPaintProjectVoMapper(
    private val miniatureVoMapper: Mapper<MiniatureBo, StartPaintMiniatureVo>
) : Mapper<ProjectBo, StartPaintProjectVo>() {
    override fun transform(data: ProjectBo): StartPaintProjectVo = StartPaintProjectVo(
        id = data.id,
        name = data.name,minis = data.minis.map { miniatureBo ->
            miniatureVoMapper.transform(miniatureBo)

        }
    )

    override fun transformReverse(data: StartPaintProjectVo): ProjectBo = ProjectBo(
        id = data.id,
        name = data.name,
        minis = data.minis.map { miniatureVo ->
            miniatureVoMapper.transformReverse(miniatureVo)

        }
    )
}
