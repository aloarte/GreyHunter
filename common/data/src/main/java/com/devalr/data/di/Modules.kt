package com.devalr.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.devalr.data.database.GreyHunterDatabase
import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.project.ProjectDao
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val dataSourcesModule = module {

}

private val databaseModule = module {
    single { provideDataBase(androidApplication()) }
    single { provideProjectsDao(get()) }
    single { provideMiniaturesDao(get()) }
}

private fun provideDataBase(application: Application): GreyHunterDatabase =
    Room.databaseBuilder(
        application,
        GreyHunterDatabase::class.java,
        "GreyHunterDatabase"
    ).fallbackToDestructiveMigration().build()


private fun provideProjectsDao(dataBase: GreyHunterDatabase): ProjectDao = dataBase.projectDao()

private fun provideMiniaturesDao(dataBase: GreyHunterDatabase): MiniatureDao =
    dataBase.miniatureDao()

private val Context.dataStore by preferencesDataStore(name = "settings")

private val dataFrameworkModule = module {
    single<DataStore<Preferences>> {
        androidContext().dataStore
    }
}
val dataModules = module {
    includes(databaseModule, dataSourcesModule, dataFrameworkModule)
}
