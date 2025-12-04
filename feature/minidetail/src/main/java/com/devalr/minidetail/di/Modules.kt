package com.devalr.minidetail.di

import com.devalr.domain.di.domainModules
import org.koin.dsl.module

private val viewModelModules = module {

}

val featureMiniDetailModules = module {
    includes(domainModules, viewModelModules)
}
