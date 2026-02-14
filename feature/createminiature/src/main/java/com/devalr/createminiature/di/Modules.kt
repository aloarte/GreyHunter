package com.devalr.createminiature.di

import com.devalr.createminiature.AddMiniatureViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        AddMiniatureViewModel(get(), get(), get(), get())
    }
}

val featureAddMiniatureModules = module {
    includes(viewModelModules)
}
