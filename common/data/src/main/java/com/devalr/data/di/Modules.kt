package com.devalr.data.di

import org.koin.dsl.module

private val dataSourcesModule = module {

}

private val databaseModule = module {
}


private val dataFrameworkModule = module {

}

val dataModules = module {
    includes(databaseModule, dataSourcesModule, dataFrameworkModule)
}
