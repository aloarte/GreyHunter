package com.devalr.createproject.di

import com.devalr.createproject.AddProjectViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        AddProjectViewModel(get(), get(), get())
    }
}

val featureAddProjectModules = module {
    includes(viewModelModules)
}
