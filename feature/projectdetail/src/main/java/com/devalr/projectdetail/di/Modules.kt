package com.devalr.projectdetail.di

import com.devalr.domain.di.domainModules
import com.devalr.projectdetail.ProjectDetailViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        ProjectDetailViewModel(get(),get())
    }
}

val featureProjectDetailModules = module {
    includes(domainModules, viewModelModules)
}
