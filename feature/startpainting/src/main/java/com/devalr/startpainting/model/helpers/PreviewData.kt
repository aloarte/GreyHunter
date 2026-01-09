package com.devalr.startpainting.model.helpers

import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo

private const val HIEROTEK_PROJECT_ID = 1L
private const val SA_PROJECT_ID = 2L


// --- HIEROTEK ---
val technomancerVo = StartPaintMiniatureVo(
    id = 1L,
    isSelected = false,
    projectId = HIEROTEK_PROJECT_ID,
    name = "Technomancer"
)

val chronomancerVo = StartPaintMiniatureVo(
    id = 1L,
    isSelected = false,
    projectId = HIEROTEK_PROJECT_ID,
    name = "Chronomancer"
)

val deathmarkVo = StartPaintMiniatureVo(
    id = 1L,
    isSelected = false,
    projectId = HIEROTEK_PROJECT_ID,
    name = "Deathmark"
)

val immortalVo = StartPaintMiniatureVo(
    id = 1L,
    isSelected = false,
    projectId = HIEROTEK_PROJECT_ID,
    name = "Immortal Guardian"
)

val hierotekCircleProjectVo = StartPaintProjectVo(
    id = HIEROTEK_PROJECT_ID,
    name = "Hierotek Circle Killteam",
    minis = listOf(technomancerVo, chronomancerVo, deathmarkVo, immortalVo)
)


// --- STORMLIGHT ARCHIVE ---

val parshendiVo = StartPaintMiniatureVo(
    id = 1L,
    isSelected = false,
    projectId = SA_PROJECT_ID,
    name = "Parshendi"
)

val alethiVo = StartPaintMiniatureVo(
    id = 1L,
    isSelected = false,
    projectId = SA_PROJECT_ID,
    name = "Alethi"
)

val stormlightArchiveProjectVo = StartPaintProjectVo(
    id = SA_PROJECT_ID,
    name = "Stormlight Archive RPG minis",
    minis = listOf(parshendiVo, alethiVo)
)

