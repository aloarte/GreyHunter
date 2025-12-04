package com.devalr.greyhunter.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("AppContext")) { androidContext() }
}
