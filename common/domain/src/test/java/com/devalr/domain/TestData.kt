package com.devalr.domain

import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectEntity
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData

object TestData {


    private const val MINI_NAME = "Space marine"
    private const val MINI_COMPLETION_PERCENTAGE = 0.5f
    private const val PROJECT_NAME = "My First Army"
    private const val PROJECT_COMPLETION_PERCENTAGE = 0.5f
    const val PROJECT_ID = 10L
    const val MINI_1_ID = 1L
    const val MINI_2_ID = 2L


    val mini1Bo = MiniatureBo(
        id = MINI_1_ID,
        projectId = PROJECT_ID,
        name = MINI_NAME,
        percentage = MINI_COMPLETION_PERCENTAGE,
        imageUri = null,
        completion = MiniCompletionBo(
            isAssembled = true,
            isPrimed = true,
            isBaseColored = true,
            isDetailed = true,
            baseIsFinished = true
        )
    )

    val mini2Bo = MiniatureBo(
        id = MINI_2_ID,
        projectId = PROJECT_ID,
        name = MINI_NAME,
        percentage = MINI_COMPLETION_PERCENTAGE,
        imageUri = null,
        completion = MiniCompletionBo(
            isAssembled = false,
            isPrimed = false,
            isBaseColored = false,
            isDetailed = false,
            baseIsFinished = false
        )
    )

    val mini1Entity = MiniatureEntity(
        id = MINI_1_ID,
        projectId = PROJECT_ID,
        name = MINI_NAME,
        completionPercentage = MINI_COMPLETION_PERCENTAGE,
        imageUri = null,
        isAssembled = true,
        isPrimed = true,
        isBaseColored = true,
        isDetailed = true,
        baseIsFinished = true
    )

    val mini2Entity = MiniatureEntity(
        id = MINI_2_ID,
        projectId = PROJECT_ID,
        name = MINI_NAME,
        completionPercentage = MINI_COMPLETION_PERCENTAGE,
        imageUri = null,
        isAssembled = false,
        isPrimed = false,
        isBaseColored = false,
        isDetailed = false,
        baseIsFinished = false
    )

    val projectBo = ProjectBo(
        id = PROJECT_ID,
        name = PROJECT_NAME,
        imageUri = null,
        percentage = PROJECT_COMPLETION_PERCENTAGE,
        minis = listOf(mini1Bo, mini2Bo)
    )

    val projectEntity = ProjectEntity(
        id = PROJECT_ID,
        name = PROJECT_NAME,
        imageUri = null,
        completionPercentage = PROJECT_COMPLETION_PERCENTAGE,
    )

    val projectEntityData = ProjectEntityData(
        projectEntity = projectEntity,
        miniatureEntities = listOf(mini1Entity, mini2Entity)
    )

    val projectEntityDataEmptyMiniatures = ProjectEntityData(
        projectEntity = projectEntity,
        miniatureEntities = emptyList()
    )

}