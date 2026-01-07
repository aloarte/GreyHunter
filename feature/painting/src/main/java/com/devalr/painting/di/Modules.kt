package com.devalr.painting.di

import com.devalr.domain.di.domainModules
import com.devalr.painting.PaintingViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        PaintingViewModel(get())
    }
}

val featurePaintingModules = module {
    includes(domainModules, viewModelModules)
}
