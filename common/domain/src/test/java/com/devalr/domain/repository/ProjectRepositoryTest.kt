package com.devalr.domain.repository

import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.project.ProjectDao
import com.devalr.domain.ProjectRepositoryImpl
import com.devalr.domain.TestData.PROJECT_ID
import com.devalr.domain.TestData.mini1Entity
import com.devalr.domain.TestData.mini2Entity
import com.devalr.domain.TestData.projectBo
import com.devalr.domain.TestData.projectEntity
import com.devalr.domain.TestData.projectEntityData
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class ProjectRepositoryTest {

    private val projectDao: ProjectDao = mockk()

    private val miniatureDao: MiniatureDao = mockk()

    private val mapper: Mapper<ProjectEntityData, ProjectBo> = mockk()

    private val fixedClock = Clock.fixed(Instant.parse("2022-01-01T00:00:00Z"), ZoneId.of("UTC"))

    private val date = 1640995200000L

    private lateinit var repository: ProjectRepositoryImpl


    @Before
    fun setUp() {
        repository = ProjectRepositoryImpl(projectDao, miniatureDao, mapper, fixedClock)
        every { mapper.transform(projectEntityData) } returns projectBo
        every { mapper.transformReverse(projectBo) } returns projectEntityData
    }

    @Test
    fun `GIVEN empty database WHEN addProject is called THEN project is inserted into database`() =
        runTest {
            // GIVEN
            coEvery { projectDao.insertProject(projectEntity) } returns PROJECT_ID

            // WHEN
            val result = repository.addProject(projectBo)

            // THEN
            verify(exactly = 1) { mapper.transformReverse(projectBo) }
            coVerify(exactly = 1) { projectDao.insertProject(projectEntity) }
            assertEquals(PROJECT_ID, result)
        }

    @Test
    fun `GIVEN project inserted WHEN getProjectById is called THEN project is returned`() =
        runTest {
            // GIVEN
            coEvery { projectDao.getProjectById(PROJECT_ID) } returns flowOf(projectEntity)
            coEvery { miniatureDao.getMiniaturesByProject(PROJECT_ID) } returns
                    flowOf(listOf(mini1Entity, mini2Entity))

            // WHEN
            val resultFlow = repository.getProject(PROJECT_ID)
            val resultBo = resultFlow.first()

            // THEN
            coVerify(exactly = 1) { projectDao.getProjectById(PROJECT_ID) }
            coVerify(exactly = 1) { miniatureDao.getMiniaturesByProject(PROJECT_ID) }
            verify(exactly = 1) { mapper.transform(projectEntityData) }
            assertEquals(projectBo, resultBo)
            assertEquals(2, projectBo.minis.size)
        }

    @Test
    fun `GIVEN list of projects WHEN getAllProjects is called THEN returns mapped list flow`() =
        runTest {
            // GIVEN
            coEvery { projectDao.getAllProjects() } returns flowOf(listOf(projectEntity))
            coEvery { miniatureDao.getMiniaturesByProject(projectEntity.id) } returns flowOf(
                listOf(mini1Entity, mini2Entity)
            )
            every { mapper.transform(projectEntityData) } returns projectBo

            // WHEN
            val resultFlow = repository.getAllProjects()
            val resultList = resultFlow.first()

            // THEN
            coVerify(exactly = 1) { projectDao.getAllProjects() }
            coVerify(exactly = 1) { miniatureDao.getMiniaturesByProject(PROJECT_ID) }
            verify(exactly = 1) { mapper.transform(projectEntityData) }
            assertEquals(2, projectBo.minis.size)
            assertEquals(projectBo, resultList[0])
        }

    @Test
    fun `GIVEN project inserted WHEN updateProject is called and updates the project THEN returns true`() =
        runTest {
            // GIVEN
            coEvery { projectDao.updateProject(projectEntity.copy(lastUpdate = date)) } returns 1

            // WHEN
            val result = repository.updateProject(projectBo)

            // THEN
            verify(exactly = 1) { mapper.transformReverse(projectBo) }
            coVerify(exactly = 1) { projectDao.updateProject(projectEntity.copy(lastUpdate = date)) }
            assertTrue(result)
        }

    @Test
    fun `GIVEN project inserted WHEN updateProject is called and does not update the project THEN returns false`() =
        runTest {
            // GIVEN
            coEvery { projectDao.updateProject(projectEntity.copy(lastUpdate = date)) } returns 0

            // WHEN
            val result = repository.updateProject(projectBo)

            // THEN
            verify(exactly = 1) { mapper.transformReverse(projectBo) }
            coVerify(exactly = 1) { projectDao.updateProject(projectEntity.copy(lastUpdate = date)) }
            assertFalse(result)
        }

    @Test
    fun `WHEN deleteProject is called THEN call remove in database returns true`() = runTest {
        // GIVEN
        coEvery { projectDao.deleteProject(1L) } returns 1

        // WHEN
        val result = repository.deleteProject(1L)

        // THEN
        coVerify(exactly = 1) { projectDao.deleteProject(1L) }
        assertTrue(result)
    }

    @Test
    fun `WHEN deleteProject is called THEN call remove in database returns false`() = runTest {
        // GIVEN
        coEvery { projectDao.deleteProject(1L) } returns 0

        // WHEN
        val result = repository.deleteProject(1L)

        // THEN
        coVerify(exactly = 1) { projectDao.deleteProject(1L) }
        assertFalse(result)
    }
}