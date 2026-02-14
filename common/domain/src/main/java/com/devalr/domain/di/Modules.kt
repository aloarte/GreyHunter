package com.devalr.domain.di

import android.content.ContentResolver
import android.content.Context
import com.devalr.domain.file.CSVManager
import org.koin.dsl.module

private val external = module {
    single { provideCSVManager() }
    single { provideContentResolver(get()) }
}

private fun provideContentResolver(context: Context):ContentResolver = context.contentResolver

private fun provideCSVManager(): CSVManager = CSVManager()

val domainModules = module {
    includes(external)
}
