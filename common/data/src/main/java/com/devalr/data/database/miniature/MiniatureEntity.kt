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
    val completionPercentage: Float,
    val imageUri: String? = null,
    val isPrepared: Boolean = false,
    val isBaseColored: Boolean = false,
    val hasDetails: Boolean = false,
    val isBased: Boolean = false
)