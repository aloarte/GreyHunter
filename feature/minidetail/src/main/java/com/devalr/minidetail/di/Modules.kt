package com.devalr.minidetail.di

import com.devalr.domain.di.domainModules
import com.devalr.minidetail.MiniatureDetailViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        MiniatureDetailViewModel(get(), get(), get())
    }

}

val featureMiniDetailModules = module {
    includes(domainModules, viewModelModules)
}
