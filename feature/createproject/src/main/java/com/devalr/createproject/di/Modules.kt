package com.devalr.createproject.di

import com.devalr.createproject.AddProjectViewModel
import org.koin.dsl.module
import com.devalr.domain.di.domainModules
private val viewModelModules = module {
    factory {
        AddProjectViewModel(get())
    }
}

val featureAddProjectModules = module {
    includes(domainModules, viewModelModules)
}
