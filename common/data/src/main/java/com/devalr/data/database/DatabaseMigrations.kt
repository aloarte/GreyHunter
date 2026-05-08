package com.devalr.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(2, 3) {

        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE miniatures ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0".trimIndent())
            db.execSQL("UPDATE miniatures SET sortOrder = id".trimIndent())
        }
    }
}