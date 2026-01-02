package com.devalr.startpainting.di

import com.devalr.domain.di.domainModules
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.startpainting.StartPaintingViewModel
import com.devalr.startpainting.mapper.StartPaintMiniatureVoMapper
import com.devalr.startpainting.mapper.StartPaintProjectVoMapper
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        StartPaintingViewModel(
            get(),
            get(named("PaintProjectVoMapper")),
            get(named("PaintMiniatureVoMapper"))
        )
    }
}

private val mapperVoModules = module {
    factory<Mapper<MiniatureBo, StartPaintMiniatureVo>>(named("PaintMiniatureVoMapper")) {
        StartPaintMiniatureVoMapper()
    }

    factory<Mapper<ProjectBo, StartPaintProjectVo>>(named("PaintProjectVoMapper")) {
        StartPaintProjectVoMapper(get(named("PaintMiniatureVoMapper")))
    }
}
val featureStartPaintingModules = module {
    includes(domainModules, viewModelModules, mapperVoModules)
}
