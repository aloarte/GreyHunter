package com.devalr.domain.model.helpers

import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo

private const val HIEROTEK_PROJECT_ID = 1L
private const val SA_PROJECT_ID = 2L


// --- HIEROTEK ---
val technomancer = MiniatureBo(
    projectId = HIEROTEK_PROJECT_ID,
    name = "Technomancer",
    percentage = 0.8f,
    completion = MiniCompletionBo(
        isAssembled = true,
        isPrimed = true,
        isBaseColored = true,
        isDetailed = true,
        baseIsFinished = false
    )
)

val chronomancer = MiniatureBo(
    projectId = HIEROTEK_PROJECT_ID,
    name = "Chronomancer",
    percentage = 1.0f,
    completion = MiniCompletionBo(
        isAssembled = true,
        isPrimed = true,
        isBaseColored = true,
        isDetailed = true,
        baseIsFinished = true
    )
)

val deathmark = MiniatureBo(
    projectId = HIEROTEK_PROJECT_ID,
    name = "Deathmark",
    percentage = 0.1f,
    completion = MiniCompletionBo(
        isAssembled = true,
        isPrimed = false,
        isBaseColored = false,
        isDetailed = false,
        baseIsFinished = false
    )
)

val immortal = MiniatureBo(
    projectId = HIEROTEK_PROJECT_ID,
    name = "Immortal Guardian",
    percentage = 0.4f,
    completion = MiniCompletionBo(
        isAssembled = true,
        isPrimed = true,
        isBaseColored = false,
        isDetailed = false,
        baseIsFinished = false
    )
)

val hierotekCircleProject = ProjectBo(
    id = HIEROTEK_PROJECT_ID,
    name = "Hierotek Circle Killteam",
    description = "This is a project of the Necron Hierotek Circle for Killteam games.",
    percentage = 0.68f,
    minis = listOf(technomancer, chronomancer, deathmark, immortal)
)


// --- STORMLIGHT ARCHIVE ---

val parshendi = MiniatureBo(
    projectId = SA_PROJECT_ID,
    name = "Parshendi",
    percentage = 0.5f,
    imageUri = "sampleUri",
    completion = MiniCompletionBo(
        isAssembled = true,
        isPrimed = true,
        isBaseColored = false,
        isDetailed = false,
        baseIsFinished = false
    )
)

val alethi = MiniatureBo(
    projectId = SA_PROJECT_ID,
    name = "Alethi",
    percentage = 0.3f,
    completion = MiniCompletionBo(
        isAssembled = true,
        isPrimed = false,
        isBaseColored = false,
        isDetailed = false,
        baseIsFinished = false
    )
)

val stormlightArchiveProject = ProjectBo(
    id = SA_PROJECT_ID,
    imageUri = "sampleUri",
    name = "Stormlight Archive RPG minis",
    description = "Miniatures of the TRPG of Stormlight Archive series. This minis came mounted.",
    percentage = 0.36f,
    minis = listOf(parshendi, alethi)
)

