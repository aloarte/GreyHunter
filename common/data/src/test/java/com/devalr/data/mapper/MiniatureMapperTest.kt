package com.devalr.data.mapper

import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.data.mappers.MiniatureMapper
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import org.junit.Assert.assertEquals
import org.junit.Test

class MiniatureMapperTest {

    private val mapper = MiniatureMapper()

    private companion object {
        const val ID = 1L
        const val PROJECT_ID = 100L
        const val NAME = "Space Marine Captain"
        const val IMAGE_URI = "content://images/123"
        const val PERCENTAGE = 0.8f
        const val IS_ASSEMBLED = true
        const val IS_PRIMED = true
        const val IS_BASE_COLORED = false
        const val IS_DETAILED = true
        const val BASE_IS_FINISHED = false
    }

    @Test
    fun `GIVEN a MiniatureEntity WHEN transform is called THEN returns correct MiniatureBo`() {
        // GIVEN
        val entity = MiniatureEntity(
            id = ID,
            projectId = PROJECT_ID,
            name = NAME,
            completionPercentage = PERCENTAGE,
            imageUri = IMAGE_URI,
            isAssembled = IS_ASSEMBLED,
            isPrimed = IS_PRIMED,
            isBaseColored = IS_BASE_COLORED,
            isDetailed = IS_DETAILED,
            baseIsFinished = BASE_IS_FINISHED
        )

        // WHEN
        val bo = mapper.transform(entity)

        // THEN
        assertEquals(ID, bo.id)
        assertEquals(PROJECT_ID, bo.projectId)
        assertEquals(NAME, bo.name)
        assertEquals(IMAGE_URI, bo.imageUri)
        assertEquals(PERCENTAGE, bo.percentage, 0.0f)
        assertEquals(IS_ASSEMBLED, bo.completion.isAssembled)
        assertEquals(IS_PRIMED, bo.completion.isPrimed)
        assertEquals(IS_BASE_COLORED, bo.completion.isBaseColored)
        assertEquals(IS_DETAILED, bo.completion.isDetailed)
        assertEquals(BASE_IS_FINISHED, bo.completion.baseIsFinished)
    }

    @Test
    fun `GIVEN a MiniatureBo WHEN transformReverse is called THEN returns correct MiniatureEntity`() {
        // GIVEN
        val bo = MiniatureBo(
            id = ID,
            projectId = PROJECT_ID,
            name = NAME,
            imageUri = IMAGE_URI,
            percentage = PERCENTAGE,
            completion = MiniCompletionBo(
                isAssembled = IS_ASSEMBLED,
                isPrimed = IS_PRIMED,
                isBaseColored = IS_BASE_COLORED,
                isDetailed = IS_DETAILED,
                baseIsFinished = BASE_IS_FINISHED
            )
        )

        // WHEN
        val entity = mapper.transformReverse(bo)

        // THEN
        assertEquals(ID, entity.id)
        assertEquals(PROJECT_ID, entity.projectId)
        assertEquals(NAME, entity.name)
        assertEquals(IMAGE_URI, entity.imageUri)
        assertEquals(PERCENTAGE, entity.completionPercentage, 0.0f)
        assertEquals(IS_ASSEMBLED, entity.isAssembled)
        assertEquals(IS_PRIMED, entity.isPrimed)
        assertEquals(IS_BASE_COLORED, entity.isBaseColored)
        assertEquals(IS_DETAILED, entity.isDetailed)
        assertEquals(BASE_IS_FINISHED, entity.baseIsFinished)
    }
}