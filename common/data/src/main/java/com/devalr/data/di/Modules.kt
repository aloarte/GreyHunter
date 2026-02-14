package com.devalr.data.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.devalr.data.database.GreyHunterDatabase
import com.devalr.data.database.ProjectEntityData
import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectDao
import com.devalr.data.mappers.MiniatureMapper
import com.devalr.data.mappers.ProjectMapper
import com.devalr.data.repository.MiniatureRepositoryImpl
import com.devalr.data.repository.ProjectRepositoryImpl
import com.devalr.data.repository.SettingsRepositoryImpl
import com.devalr.domain.MiniatureRepository
import com.devalr.domain.ProjectRepository
import com.devalr.domain.SettingsRepository
import com.devalr.data.file.CSVManager
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val dataSourcesModule = module {
    // Unused on this project
}

private val repositoriesModules = module {
    factory<MiniatureRepository> {
        MiniatureRepositoryImpl(
            get(),
            get(named("MiniatureMapper"))
        )
    }

    factory<ProjectRepository> {
        ProjectRepositoryImpl(
            get(),
            get(),
            get(named("ProjectMapper")),
            get(named("MiniatureMapper"))
        )
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
}

private val mapperModules = module {
    factory<Mapper<MiniatureEntity, MiniatureBo>>(named("MiniatureMapper")) {
        MiniatureMapper()
    }

    factory<Mapper<ProjectEntityData, ProjectBo>>(named("ProjectMapper")) {
        ProjectMapper(get(named("MiniatureMapper")))
    }
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


private val external = module {
    single<DataStore<Preferences>> {
        androidContext().dataStore
    }
    single { provideCSVManager() }
    single { provideContentResolver(get()) }
}

private fun provideContentResolver(context: Context): ContentResolver = context.contentResolver

private fun provideCSVManager(): CSVManager = CSVManager()
val dataModules = module {
    includes(repositoriesModules, mapperModules, databaseModule, dataSourcesModule, external)
}
