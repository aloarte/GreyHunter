package com.devalr.painting.di

import com.devalr.painting.PaintingViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        PaintingViewModel(get(), get())
    }
}

val featurePaintingModules = module {
    includes(viewModelModules)
}
