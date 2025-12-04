package com.devalr.domain.di

import com.devalr.data.di.dataModules
import org.koin.dsl.module

private val repositoriesModules = module {

}

private val usecasesModules = module {

}

private val mapperModules = module {
}


val domainModules = module {
    includes(mapperModules, usecasesModules, repositoriesModules, dataModules)
}
