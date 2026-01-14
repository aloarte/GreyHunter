package com.devalr.home.di

import com.devalr.domain.di.domainModules
import com.devalr.home.HomeViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        HomeViewModel(get(), get())
    }
}

val featureHomeModules = module {
    includes(domainModules, viewModelModules)
}
