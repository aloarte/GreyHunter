package com.devalr.data.file

import com.devalr.data.TestData.MINI_1_ID
import com.devalr.data.TestData.MINI_2_ID
import com.devalr.data.TestData.mini1Bo
import com.devalr.data.TestData.mini2Bo
import com.devalr.data.TestData.projectBo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class CSVManagerTest {

    private lateinit var csvManager: CSVManager

    @Before
    fun setUp() {
        csvManager = CSVManager()
    }

    @Test
    fun `GIVEN TestData project WHEN writeProjectsToCSV is called THEN CSV contains project and mini data`() =
        runTest {
            // GIVEN
            val projects = listOf(projectBo)
            val outputStream = ByteArrayOutputStream()

            // WHEN
            val result = csvManager.writeProjectsToCSV(outputStream, projects)
            val csvContent = outputStream.toString()

            // THEN
            assertTrue(result)
            assertTrue(csvContent.contains(projectBo.id.toString()))
            assertTrue(csvContent.contains(projectBo.name))
            assertTrue(csvContent.contains(MINI_1_ID.toString()))
            assertTrue(csvContent.contains(mini1Bo.name))
            assertTrue(csvContent.contains("true,true,true,true,true"))
            assertTrue(csvContent.contains(MINI_2_ID.toString()))
            assertTrue(csvContent.contains("false,false,false,false,false"))
        }

    @Test
    fun `GIVEN CSV generated from TestData WHEN readProjectsFromCSV is called THEN project and minis are correctly parsed`() =
        runTest {
            // GIVEN
            val outputStream = ByteArrayOutputStream()
            csvManager.writeProjectsToCSV(outputStream, listOf(projectBo))
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())

            // WHEN
            val projects = csvManager.readProjectsFromCSV(inputStream)

            // THEN
            assertEquals(1, projects.size)
            val project = projects.first()
            assertEquals(projectBo.id, project.id)
            assertEquals(projectBo.name, project.name)
            assertEquals(projectBo.progress, project.progress)
            assertEquals(2, project.minis.size)
            val mini1 = project.minis.first { it.id == MINI_1_ID }
            val mini2 = project.minis.first { it.id == MINI_2_ID }
            assertEquals(mini1Bo.id, mini1.id)
            assertEquals(mini1Bo.projectId, mini1.projectId)
            assertEquals(mini1Bo.name, mini1.name)
            assertEquals(mini1Bo.percentage, mini1.percentage)
            assertEquals(
                mini1Bo.completion.isAssembled,
                mini1.completion.isAssembled
            )
            assertEquals(
                mini1Bo.completion.isPrimed,
                mini1.completion.isPrimed
            )
            assertEquals(
                mini1Bo.completion.isBaseColored,
                mini1.completion.isBaseColored
            )
            assertEquals(
                mini1Bo.completion.isDetailed,
                mini1.completion.isDetailed
            )
            assertEquals(
                mini1Bo.completion.baseIsFinished,
                mini1.completion.baseIsFinished
            )
            assertEquals(mini2Bo.id, mini2.id)
            assertEquals(mini2Bo.projectId, mini2.projectId)
            assertEquals(mini2Bo.name, mini2.name)
            assertEquals(mini2Bo.percentage, mini2.percentage)
            assertEquals(
                mini2Bo.completion.isAssembled,
                mini2.completion.isAssembled
            )
            assertEquals(
                mini2Bo.completion.isPrimed,
                mini2.completion.isPrimed
            )
            assertEquals(
                mini2Bo.completion.isBaseColored,
                mini2.completion.isBaseColored
            )
            assertEquals(
                mini2Bo.completion.isDetailed,
                mini2.completion.isDetailed
            )
            assertEquals(
                mini2Bo.completion.baseIsFinished,
                mini2.completion.baseIsFinished
            )
        }

    @Test
    fun `GIVEN TestData project WHEN write and read are executed THEN result matches original TestData`() =
        runTest {
            // GIVEN
            val originalProject = projectBo
            val outputStream = ByteArrayOutputStream()

            // WHEN
            csvManager.writeProjectsToCSV(outputStream, listOf(originalProject))
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())
            val parsedProjects = csvManager.readProjectsFromCSV(inputStream)

            // THEN
            assertEquals(1, parsedProjects.size)
            val parsed = parsedProjects.first()
            assertEquals(originalProject.id, parsed.id)
            assertEquals(originalProject.name, parsed.name)
            assertEquals(originalProject.progress, parsed.progress)
            assertEquals(originalProject.minis.size, parsed.minis.size)
            originalProject.minis.forEach { originalMini ->
                val parsedMini = parsed.minis.first { it.id == originalMini.id }
                assertEquals(originalMini.id, parsedMini.id)
                assertEquals(originalMini.projectId, parsedMini.projectId)
                assertEquals(originalMini.name, parsedMini.name)
                assertEquals(originalMini.percentage, parsedMini.percentage)
                assertEquals(
                    originalMini.completion.isAssembled,
                    parsedMini.completion.isAssembled
                )
                assertEquals(
                    originalMini.completion.isPrimed,
                    parsedMini.completion.isPrimed
                )
                assertEquals(
                    originalMini.completion.isBaseColored,
                    parsedMini.completion.isBaseColored
                )
                assertEquals(
                    originalMini.completion.isDetailed,
                    parsedMini.completion.isDetailed
                )
                assertEquals(
                    originalMini.completion.baseIsFinished,
                    parsedMini.completion.baseIsFinished
                )
            }
        }
}