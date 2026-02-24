package com.devalr.data.file

import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

class CSVManager {

    suspend fun writeProjectsToCSV(
        outputStream: OutputStream,
        projects: List<ProjectBo>
    ): Boolean = withContext(Dispatchers.IO) {

        OutputStreamWriter(outputStream, Charsets.UTF_8).use { writer ->

            val printer = CSVPrinter(
                writer,
                CSVFormat.DEFAULT.builder().build()
            )

            // ---- PROJECTS ----
            printer.printRecord(CSV_TITLE_PROJECT)
            printer.printRecord("id", "name", "description", "progress")

            projects.forEach { project ->
                printer.printRecord(
                    project.id,
                    project.name,
                    project.description ?: "",
                    project.progress
                )
            }

            printer.println()

            // ---- MINIATURES ----
            printer.printRecord(CSV_TITLE_MINIATURES)
            printer.printRecord(
                "id",
                "projectId",
                "name",
                "percentage",
                "isAssembled",
                "isPrimed",
                "isBaseColored",
                "isDetailed",
                "baseIsFinished"
            )

            projects
                .flatMap { it.minis }
                .forEach { mini ->
                    printer.printRecord(
                        mini.id,
                        mini.projectId,
                        mini.name,
                        mini.percentage,
                        mini.completion.isAssembled,
                        mini.completion.isPrimed,
                        mini.completion.isBaseColored,
                        mini.completion.isDetailed,
                        mini.completion.baseIsFinished
                    )
                }

            printer.flush()
        }

        true
    }

    suspend fun readProjectsFromCSV(
        inputStream: InputStream
    ): List<ProjectBo> = withContext(Dispatchers.IO) {

        val projects = mutableMapOf<Long, ProjectBo>()
        val minis = mutableListOf<MiniatureBo>()

        InputStreamReader(inputStream, Charsets.UTF_8).use { reader ->

            val parser = CSVParser(
                reader,
                CSVFormat.DEFAULT.builder().build()
            )

            var section: CSVSection? = null
            var skipNext = false

            for (record in parser) {

                if (record.size() == 1) {
                    when (record.get(0)) {
                        CSV_TITLE_PROJECT -> {
                            section = CSVSection.PROJECTS
                            skipNext = true
                            continue
                        }

                        CSV_TITLE_MINIATURES -> {
                            section = CSVSection.MINIATURES
                            skipNext = true
                            continue
                        }
                    }
                }

                if (skipNext) {
                    skipNext = false
                    continue
                }

                when (section) {

                    CSVSection.PROJECTS -> {
                        val project = ProjectBo(
                            id = record.get(0).toLong(),
                            name = record.get(1),
                            description = record.get(2).ifEmpty { null },
                            progress = record.get(3).toFloat(),
                            minis = emptyList()
                        )
                        projects[project.id] = project
                    }

                    CSVSection.MINIATURES -> {
                        minis += MiniatureBo(
                            id = record.get(0).toLong(),
                            projectId = record.get(1).toLong(),
                            name = record.get(2),
                            percentage = record.get(3).toFloat(),
                            completion = MiniCompletionBo(
                                isAssembled = record.get(4).toBoolean(),
                                isPrimed = record.get(5).toBoolean(),
                                isBaseColored = record.get(6).toBoolean(),
                                isDetailed = record.get(7).toBoolean(),
                                baseIsFinished = record.get(8).toBoolean()
                            )
                        )
                    }

                    null -> Unit
                }
            }
        }

        projects.values.map { project ->
            project.copy(
                minis = minis.filter { it.projectId == project.id }
            )
        }
    }
}