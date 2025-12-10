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
class MiniDaoTest {

    private lateinit var db: GreyHunterDatabase
    private lateinit var projectDao: ProjectDao
    private lateinit var miniatureDao: MiniatureDao

    companion object {
        private const val PROJECT_NAME = "Necron Army"
        private const val MINI_NAME = "Overlord"
        private const val NEW_MINI_NAME = "Plasmancer"
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GreyHunterDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        projectDao = db.projectDao()
        miniatureDao = db.miniatureDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `GIVEN a project WHEN a miniature is inserted THEN the database contains that miniature linked to project`() =
        runBlocking {
            // GIVEN:
            val projectId = projectDao.insertProject(
                ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.0f)
            )

            // WHEN:
            val miniId = miniatureDao.insertMiniature(
                MiniatureEntity(
                    projectId = projectId,
                    name = MINI_NAME,
                    completionPercentage = 0.0f
                )
            )

            // THEN
            val insertedMini = miniatureDao.getMiniatureById(miniId).first()

            assertNotNull(insertedMini)
            assertEquals(MINI_NAME, insertedMini.name)
            assertEquals(projectId, insertedMini.projectId)
        }

    @Test
    fun `GIVEN a stored miniature WHEN is updated THEN database gets the new values`() =
        runBlocking {
            // GIVEN
            val projectId = projectDao.insertProject(
                ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.0f)
            )
            val miniId = miniatureDao.insertMiniature(
                MiniatureEntity(
                    projectId = projectId,
                    name = MINI_NAME,
                    completionPercentage = 0.1f
                )
            )

            // WHEN
            miniatureDao.getMiniatureById(miniId).first()?.let { currentMini ->
                miniatureDao.updateMiniature(
                    currentMini.copy(
                        name = NEW_MINI_NAME,
                        completionPercentage = 1.0f,
                        isPrimed = true
                    )
                )
            } ?: fail("Miniature not found initially")

            // THEN
            miniatureDao.getMiniatureById(miniId).first()?.let { updatedMini ->
                assertEquals(NEW_MINI_NAME, updatedMini.name)
                assertEquals(1.0f, updatedMini.completionPercentage)
                assertTrue(updatedMini.isPrimed)
            } ?: fail("Miniature not found after update")
        }

    @Test
    fun `GIVEN a stored miniature WHEN is removed THEN database does not find it anymore`() =
        runBlocking {
            // GIVEN
            val projectId = projectDao.insertProject(
                ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.0f)
            )
            val miniId = miniatureDao.insertMiniature(
                MiniatureEntity(
                    projectId = projectId,
                    name = MINI_NAME,
                    completionPercentage = 0.0f
                )
            )

            // WHEN
            miniatureDao.deleteMiniature(miniId)

            // THEN
            val result = miniatureDao.getMiniatureById(miniId).first()
            assertNull(result)
        }

    @Test
    fun `GIVEN multiple miniatures WHEN querying by project THEN returns all miniatures for that project`() =
        runBlocking {
            // GIVEN
            val projectId = projectDao.insertProject(
                ProjectEntity(name = PROJECT_NAME, completionPercentage = 0.0f)
            )

            val minisToInsert = listOf("Warrior 1", "Warrior 2", "Warrior 3")
            minisToInsert.forEach { name ->
                miniatureDao.insertMiniature(
                    MiniatureEntity(projectId = projectId, name = name, completionPercentage = 0.0f)
                )
            }

            // WHEN
            val projectMinis = miniatureDao.getMiniaturesByProject(projectId).first()

            // THEN
            assertEquals(3, projectMinis.size)
        }
}