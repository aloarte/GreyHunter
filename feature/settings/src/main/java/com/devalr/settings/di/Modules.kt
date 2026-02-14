package com.devalr.settings.di

import com.devalr.settings.SettingsViewModel
import org.koin.dsl.module

private val viewModelModules = module {
    factory {
        SettingsViewModel(get(), get())
    }
}

val featureSettingsModules = module {
    includes(viewModelModules)
}
