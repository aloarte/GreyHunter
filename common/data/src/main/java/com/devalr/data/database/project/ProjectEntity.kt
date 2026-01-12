package com.devalr.data.database.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val imageUri: String? = null,
    val completionPercentage: Float,
    val lastUpdate: Long? = null
)