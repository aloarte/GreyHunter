package com.devalr.projectdetail.di

import com.devalr.domain.di.domainModules
import org.koin.dsl.module

private val viewModelModules = module {

}

val featureProjectDetailModules = module {
    includes(domainModules, viewModelModules)
}
