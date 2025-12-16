package com.devalr.createminiature.di

import com.devalr.createminiature.AddMiniatureViewModel
import com.devalr.domain.di.domainModules
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        AddMiniatureViewModel(get(), get())
    }
}

val featureAddMiniatureModules = module {
    includes(domainModules, viewModelModules)
}
