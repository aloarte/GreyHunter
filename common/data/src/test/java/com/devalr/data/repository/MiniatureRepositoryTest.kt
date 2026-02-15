package com.devalr.data.repository

import com.devalr.data.TestData.MINI_1_ID
import com.devalr.data.TestData.PROJECT_ID
import com.devalr.data.TestData.mini1Bo
import com.devalr.data.TestData.mini1Entity
import com.devalr.data.TestData.mini2Bo
import com.devalr.data.TestData.mini2Entity
import com.devalr.data.database.miniature.MiniatureDao
import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class MiniatureRepositoryTest {

    private val miniatureDao: MiniatureDao = mockk()
    private val mapper: Mapper<MiniatureEntity, MiniatureBo> = mockk()

    private val fixedClock = Clock.fixed(Instant.parse("2022-01-01T00:00:00Z"), ZoneId.of("UTC"))
    private val date = 1640995200000L

    private lateinit var repository: MiniatureRepositoryImpl

    @Before
    fun setUp() {
        repository = MiniatureRepositoryImpl(miniatureDao, mapper, fixedClock)
        every { mapper.transform(mini1Entity) } returns mini1Bo
        every { mapper.transform(mini2Entity) } returns mini2Bo
        every { mapper.transformReverse(mini1Bo) } returns mini1Entity
        every { mapper.transformReverse(mini2Bo) } returns mini2Entity
    }

    @After
    fun tearDown() {
        unmockkStatic(System::class)
    }

    @Test
    fun `GIVEN miniature WHEN addMiniature is THEN miniature is inserted on database`() = runTest {
        // GIVEN
        coEvery { miniatureDao.insertMiniature(mini1Entity.copy(id = 0)) } returns MINI_1_ID

        // WHEN
        val result = repository.addMiniature(mini1Bo)

        // THEN
        verify(exactly = 1) { mapper.transformReverse(mini1Bo) }
        coVerify(exactly = 1) { miniatureDao.insertMiniature(mini1Entity.copy(id = 0)) }
        assertEquals(MINI_1_ID, result)
    }

    @Test
    fun `GIVEN miniature inserted WHEN getMiniatureById is called THEN miniature is returned`() =
        runTest {
            // GIVEN
            coEvery { miniatureDao.getMiniatureById(MINI_1_ID) } returns flowOf(mini1Entity)

            // WHEN
            val resultFlow = repository.getMiniature(MINI_1_ID)
            val resultBo = resultFlow.first()

            // THEN
            coVerify(exactly = 1) { miniatureDao.getMiniatureById(MINI_1_ID) }
            verify(exactly = 1) { mapper.transform(mini1Entity) }
            assertEquals(mini1Bo, resultBo)
        }

    @Test
    fun `GIVEN list of miniatures inserted WHEN getMiniaturesFromProject is called THEN returns mapped list flow`() =
        runTest {
            // GIVEN
            coEvery { miniatureDao.getMiniaturesByProject(PROJECT_ID) } returns flowOf(
                listOf(mini1Entity, mini2Entity)
            )

            // WHEN
            val resultFlow = repository.getMiniaturesFromProject(PROJECT_ID)
            val resultList = resultFlow.first()

            // THEN
            coVerify(exactly = 1) { miniatureDao.getMiniaturesByProject(PROJECT_ID) }
            verify(exactly = 2) { mapper.transform(any()) }
            assertEquals(2, resultList.size)
            assertEquals(mini1Bo, resultList[0])
            assertEquals(mini2Bo, resultList[1])
        }

    @Test
    fun `GIVEN list of miniatures inserted WHEN getMiniatures is called THEN returns list flow`() =
        runTest {
            // GIVEN
            coEvery {
                miniatureDao.getMiniaturesByIds(
                    listOf(
                        mini1Bo.id,
                        mini2Bo.id
                    )
                )
            } returns flowOf(
                listOf(mini1Entity, mini2Entity)
            )

            // WHEN
            val resultFlow = repository.getMiniatures(listOf(mini1Bo.id, mini2Bo.id))
            val resultList = resultFlow.first()

            // THEN
            coVerify(exactly = 1) {
                miniatureDao.getMiniaturesByIds(
                    listOf(
                        mini1Bo.id,
                        mini2Bo.id
                    )
                )
            }
            verify(exactly = 2) { mapper.transform(any()) }
            assertEquals(2, resultList.size)
            assertEquals(mini1Bo, resultList[0])
            assertEquals(mini2Bo, resultList[1])
        }

    @Test
    fun `GIVEN miniatures inserted WHEN getLastUpdatedMiniatures is called THEN list of 2 miniatures are returned`() =
        runTest {
            // GIVEN
            coEvery { miniatureDao.getLastUpdatedMiniatures(2) } returns
                    flowOf(listOf(mini1Entity, mini2Entity))

            // WHEN
            val resultFlow = repository.getLastUpdatedMiniatures(2)
            val miniBoList = resultFlow.first()

            // THEN
            coVerify(exactly = 1) { miniatureDao.getLastUpdatedMiniatures(2) }
            verify(exactly = 1) { mapper.transform(mini1Entity) }
            verify(exactly = 1) { mapper.transform(mini2Entity) }
            assertEquals(listOf(mini1Bo, mini2Bo), miniBoList)
        }


    @Test
    fun `GIVEN miniature inserted WHEN updateMiniature is called and update the miniature THEN returns 1 row affected`() =
        runTest {
            // GIVEN
            coEvery { miniatureDao.updateMiniature(mini1Entity.copy(lastUpdate = date)) } returns 1

            // WHEN
            val result = repository.updateMiniature(mini1Bo)

            // THEN
            verify(exactly = 1) { mapper.transformReverse(mini1Bo) }
            coVerify(exactly = 1) { miniatureDao.updateMiniature(mini1Entity.copy(lastUpdate = date)) }
            assertTrue(result)
        }

    @Test
    fun `GIVEN miniature inserted WHEN updateMiniature is called and don't update the miniature THEN returns 0 row affected`() =
        runTest {
            // GIVEN
            coEvery { miniatureDao.updateMiniature(mini1Entity.copy(lastUpdate = date)) } returns 0

            // WHEN
            val result = repository.updateMiniature(mini1Bo)

            // THEN
            verify(exactly = 1) { mapper.transformReverse(mini1Bo) }
            coVerify(exactly = 1) { miniatureDao.updateMiniature(mini1Entity.copy(lastUpdate = date)) }
            assertFalse(result)
        }

    @Test
    fun `WHEN deleteMiniature is called THEN call remove in database returns true`() = runTest {
        // GIVEN
        coEvery { miniatureDao.deleteMiniature(MINI_1_ID) } returns 1

        //  GIVEN & WHEN
        val result = repository.deleteMiniature(MINI_1_ID)

        // THEN
        coVerify(exactly = 1) { miniatureDao.deleteMiniature(MINI_1_ID) }
        assertTrue(result)
    }

    @Test
    fun `WHEN deleteMiniature is called THEN call remove in database returns false`() = runTest {
        // GIVEN
        coEvery { miniatureDao.deleteMiniature(MINI_1_ID) } returns 0

        //  GIVEN & WHEN
        val result = repository.deleteMiniature(MINI_1_ID)

        // THEN
        coVerify(exactly = 1) { miniatureDao.deleteMiniature(MINI_1_ID) }
        assertFalse(result)
    }
}