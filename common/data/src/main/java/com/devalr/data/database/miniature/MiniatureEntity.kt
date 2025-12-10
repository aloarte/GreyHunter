package com.devalr.data.database.miniature

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devalr.data.database.project.ProjectEntity

@Entity(
    tableName = "miniatures",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["projectId"])]
)
data class MiniatureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val name: String,
    val completionPercentage: Float = 0f,
    val imageUri: String? = null,
    val isAssembled: Boolean = false,
    val isPrimed: Boolean = false,
    val isBaseColored: Boolean = false,
    val isDetailed: Boolean = false,
    val baseIsFinished: Boolean = false
)