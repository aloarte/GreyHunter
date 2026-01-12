package com.devalr.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.database.project.ProjectDao
import com.devalr.data.database.project.ProjectEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProjectDaoTest {

    private lateinit var db: GreyHunterDatabase
    private lateinit var projectDao: ProjectDao

    private lateinit var miniatureDao: MiniatureDao


    companion object {
        private const val PROJECT_NAME = "Hierotek Circle"
        private const val NEW_PROJECT_NAME = "Cannoptek Circle"

        private val latestProject = ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.5f, lastUpdate = 5000L)
        private val notUpdatedProject = ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.5f, lastUpdate = 0L)
        private val notLatestProject = ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.5f, lastUpdate = 4000L)
    }


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GreyHunterDatabase::class.java
        ).build()

        projectDao = db.projectDao()
        miniatureDao = db.miniatureDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `GIVEN an empty database WHEN any project is inserted THEN the database contains that project`() =
        runBlocking {
            // GIVEN & WHEN
            val projectId = projectDao.insertProject(
                ProjectEntity(
                    name = PROJECT_NAME,
                    completionPercentage = 0.0f
                )
            )

            // THEN
            val projects = projectDao.getAllProjects().first()
            val projectById = projectDao.getProjectById(projectId).first()
            assertNotNull(projects[0])
            assertNotNull(projectById)
            assertTrue(projects[0] == projectById)
        }

    @Test
    fun `GIVEN a created project WHEN project is updated THEN database gets the new result`() =
        runBlocking {
            // GIVEN
            val projectId = projectDao.insertProject(
                ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.5f)
            )

            // WHEN
            projectDao.getProjectById(projectId).firstOrNull()?.let {
                projectDao.updateProject(it.copy(name = NEW_PROJECT_NAME))
            } ?: run {
                fail("Project not found")
            }
            // THEN
            projectDao.getProjectById(projectId).firstOrNull()?.let {
                assertEquals(NEW_PROJECT_NAME, it.name)
                assertEquals(0.5f, it.completionPercentage)
            } ?: run {
                fail("Project not found")
            }
        }

    @Test
    fun `GIVEN a created project WHEN project is removed THEN database performed a cascade delete`() =
        runBlocking {
            // GIVEN
            val projectId = projectDao.insertProject(
                ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.5f)
            )
            val miniId = miniatureDao.insertMiniature(
                MiniatureEntity(
                    projectId = projectId,
                    name = "",
                    completionPercentage = 1.0f
                )
            )

            // WHEN
            projectDao.deleteProject(projectId)

            // THEN
            assertNull(projectDao.getProjectById(projectId).first())
            assertNull(miniatureDao.getMiniatureById(miniatureId = miniId).first())
        }

    @Test
    fun `GIVEN multiple projects WHEN requesting last updated THEN returns the one with highest timestamp ignoring zeros`() =
        runBlocking {
            // GIVEN
            projectDao.insertProject(latestProject)
            projectDao.insertProject(notUpdatedProject)
            projectDao.insertProject(notLatestProject)

            // WHEN
            val result = projectDao.getLastUpdatedProject().firstOrNull()

            // THEN
            assertNotNull(result)
            assertEquals(latestProject.name, result?.name)
            assertEquals(latestProject.lastUpdate, result?.lastUpdate)
        }

    @Test
    fun `GIVEN only projects with zero timestamp WHEN requesting last updated THEN returns null`() =
        runBlocking {
            // GIVEN
            projectDao.insertProject(notUpdatedProject)

            // WHEN
            val result = projectDao.getLastUpdatedProject().firstOrNull()

            // THEN
            assertNull(result)
        }
}