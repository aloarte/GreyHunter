package com.devalr.settings.di

import com.devalr.domain.di.domainModules
import com.devalr.settings.SettingsViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        SettingsViewModel(get())
    }
}

val featureSettingsModules = module {
    includes(domainModules, viewModelModules)
}
