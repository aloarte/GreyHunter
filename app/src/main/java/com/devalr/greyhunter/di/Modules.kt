package com.devalr.greyhunter.di

import com.devalr.framework.AppTracer
import com.devalr.greyhunter.MainViewModel
import com.devalr.greyhunter.tracer.FirebaseTracer
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("AppContext")) { androidContext() }
    factory<AppTracer> { FirebaseTracer() }
    factory { MainViewModel(get(), get()) }
}
