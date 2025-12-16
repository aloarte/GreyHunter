package com.devalr.domain.mapper

import com.devalr.data.database.project.ProjectEntity
import com.devalr.domain.TestData.mini1Bo
import com.devalr.domain.TestData.mini1Entity
import com.devalr.domain.TestData.mini2Bo
import com.devalr.domain.TestData.mini2Entity
import com.devalr.domain.mappers.MiniatureMapper
import com.devalr.domain.mappers.ProjectMapper
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.ProjectEntityData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectMapperTest {

    private val miniatureMapper: MiniatureMapper = mockk()
    private val mapper = ProjectMapper(miniatureMapper)

    private companion object {
        const val ID = 1L
        const val NAME = "Warhammer Army"
        const val IMAGE_URI = "content://images/project_cover"
        const val PERCENTAGE = 0.45f
    }

    @Test
    fun `GIVEN a ProjectEntityData WHEN transform is called THEN returns correct ProjectBo with mapped miniatures`() {
        // GIVEN
        val projectEntity = ProjectEntity(
            id = ID,
            name = NAME,
            completionPercentage = PERCENTAGE,
            imageUri = IMAGE_URI
        )
        every { miniatureMapper.transform(mini1Entity) } returns mini1Bo
        every { miniatureMapper.transform(mini2Entity) } returns mini2Bo
        val miniEntities = listOf(mini1Entity, mini2Entity)
        val entityData = ProjectEntityData(
            projectEntity = projectEntity,
            miniatureEntities = miniEntities
        )

        // WHEN
        val bo = mapper.transform(entityData)

        // THEN
        verify(exactly = 1) { miniatureMapper.transform(mini1Entity) }
        verify(exactly = 1) { miniatureMapper.transform(mini2Entity) }
        assertEquals(ID, bo.id)
        assertEquals(NAME, bo.name)
        assertEquals(IMAGE_URI, bo.imageUri)
        assertEquals(PERCENTAGE, bo.progress, 0.0f)
        assertEquals(2, bo.minis.size)
        assertEquals(mini1Bo, bo.minis[0])
        assertEquals(mini2Bo, bo.minis[1])
    }

    @Test
    fun `GIVEN a ProjectBo WHEN transformReverse is called THEN returns correct ProjectEntity`() {
        val bo = ProjectBo(
            id = ID,
            name = NAME,
            imageUri = IMAGE_URI,
            progress = PERCENTAGE,
            minis = listOf(mini1Bo, mini2Bo)
        )
        every { miniatureMapper.transformReverse(mini1Bo) } returns mini1Entity
        every { miniatureMapper.transformReverse(mini2Bo) } returns mini2Entity

        // WHEN
        val entity = mapper.transformReverse(bo)

        // THEN
        verify(exactly = 1) { miniatureMapper.transformReverse(mini1Bo) }
        verify(exactly = 1) { miniatureMapper.transformReverse(mini2Bo) }
        assertEquals(ID, entity.projectEntity.id)
        assertEquals(NAME, entity.projectEntity.name)
        assertEquals(IMAGE_URI, entity.projectEntity.imageUri)
        assertEquals(PERCENTAGE, entity.projectEntity.completionPercentage, 0.0f)
        assertEquals(mini1Entity, entity.miniatureEntities[0])
        assertEquals(mini2Entity, entity.miniatureEntities[1])
    }
}