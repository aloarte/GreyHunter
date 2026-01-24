package com.devalr.domain.file

import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class CSVManager {
    suspend fun writeProjectsToCSV(outputStream: OutputStream, projects: List<ProjectBo>): Boolean =
        withContext(Dispatchers.IO) {
            outputStream.bufferedWriter().use { writer ->
                writer.appendLine(CSV_TITLE_PROJECT)
                writer.appendLine(CSV_TABLE_NAMES_PROJECT)
                projects.forEach { project ->
                    writer.appendLine(
                        listOf(
                            project.id,
                            project.name.escapeCsv(),
                            project.description.escapeCsv(),
                            project.progress
                        ).joinToString(",")
                    )
                }
                writer.appendLine()
                writer.appendLine(CSV_TITLE_MINIATURES)
                writer.appendLine(CSV_TABLE_NAMES_MINIATURES)
                projects
                    .flatMap { it.minis }
                    .forEach { mini ->
                        writer.appendLine(
                            listOf(
                                mini.id,
                                mini.projectId,
                                mini.name.escapeCsv(),
                                mini.percentage,
                                mini.completion.isAssembled,
                                mini.completion.isPrimed,
                                mini.completion.isBaseColored,
                                mini.completion.isDetailed,
                                mini.completion.baseIsFinished
                            ).joinToString(",")
                        )
                    }
                return@withContext true
            }
        }

    suspend fun readProjectsFromCSV(inputStream: InputStream): List<ProjectBo> =
        withContext(Dispatchers.IO) {
            val projects = mutableMapOf<Long, ProjectBo>()
            val minis = mutableListOf<MiniatureBo>()
            inputStream.bufferedReader().useLines { lines ->
                var section: CSVSection? = null
                var isHeader = false

                lines.forEach { line ->
                    if (line.isBlank()) return@forEach

                    when {
                        line == CSV_TITLE_PROJECT -> {
                            section = CSVSection.PROJECTS
                            isHeader = true
                        }

                        line == CSV_TITLE_MINIATURES -> {
                            section = CSVSection.MINIATURES
                            isHeader = true
                        }

                        isHeader -> {
                            isHeader = false
                        }

                        else -> {
                            when (section) {
                                CSVSection.PROJECTS -> {
                                    val values = parseCsvLine(line)
                                    val project = ProjectBo(
                                        id = values[0].toLong(),
                                        name = values[1],
                                        description = values[2].ifEmpty { null },
                                        progress = values[3].toFloat(),
                                        minis = emptyList()
                                    )
                                    projects[project.id] = project
                                }

                                CSVSection.MINIATURES -> {
                                    val values = parseCsvLine(line)
                                    minis += MiniatureBo(
                                        id = values[0].toLong(),
                                        projectId = values[1].toLong(),
                                        name = values[2],
                                        percentage = values[3].toFloat(),
                                        completion = MiniCompletionBo(
                                            isAssembled = values[4].toBoolean(),
                                            isPrimed = values[5].toBoolean(),
                                            isBaseColored = values[6].toBoolean(),
                                            isDetailed = values[7].toBoolean(),
                                            baseIsFinished = values[8].toBoolean()
                                        )
                                    )
                                }

                                null -> Unit
                            }
                        }
                    }
                }
            }

            val projectsWithMinis = projects.values.map { project ->
                project.copy(minis = minis.filter { it.projectId == project.id })
            }
            return@withContext projectsWithMinis
        }

}