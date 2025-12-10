package com.devalr.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectDao
import com.devalr.data.database.project.ProjectEntity

@Database(
    entities = [ProjectEntity::class, MiniatureEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GreyHunterDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun miniatureDao(): MiniatureDao
}