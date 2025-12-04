package com.devalr.greyhunter

import android.app.Application
import com.devalr.greyhunter.di.appModule
import com.devalr.home.di.featureHomeModules
import com.devalr.minidetail.di.featureMiniDetailModules
import com.devalr.projectdetail.di.featureProjectDetailModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GreyHunterApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@GreyHunterApplication)
            // Load modules
            modules(
                appModule,
                featureHomeModules,
                featureProjectDetailModules,
                featureMiniDetailModules
            )
        }
    }
}
