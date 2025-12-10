package com.devalr.domain.di

import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.di.dataModules
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.MiniatureRepositoryImpl
import com.devalr.domain.ProjectRepository
import com.devalr.domain.ProjectRepositoryImpl
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.mappers.MiniatureMapper
import com.devalr.domain.mappers.ProjectMapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val repositoriesModules = module {
    factory<MiniatureRepository> {
        MiniatureRepositoryImpl(
            get(),
            get(named("MiniatureMapper"))
        )
    }

    factory<ProjectRepository> {
        ProjectRepositoryImpl(
            get(),
            get(),
            get(named("ProjectMapper"))
        )
    }
}

private val usecasesModules = module {

}

private val mapperModules = module {
    factory<Mapper<MiniatureEntity, MiniatureBo>>(named("MiniatureMapper")) {
        MiniatureMapper()
    }

    factory<Mapper<ProjectEntityData, ProjectBo>>(named("ProjectMapper")) {
        ProjectMapper(get(named("MiniatureMapper")))
    }
}


val domainModules = module {
    includes(mapperModules, usecasesModules, repositoriesModules, dataModules)
}
