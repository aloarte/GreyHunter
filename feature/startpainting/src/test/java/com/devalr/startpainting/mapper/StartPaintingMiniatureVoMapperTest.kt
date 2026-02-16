package com.devalr.startpainting.mapper

import com.devalr.domain.model.MiniatureBo
import com.devalr.startpainting.model.StartPaintMiniatureVo
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class StartPaintMiniatureVoMapperTest {

    private lateinit var mapper: StartPaintMiniatureVoMapper

    private val miniatureBo = MiniatureBo(
        id = 12L,
        projectId = 1L,
        name = "mini",
        imageUri = "uri://image"
    )

    private val miniatureVo = StartPaintMiniatureVo(
        id = 12L,
        projectId = 1L,
        name = "mini",
        imageUri = "uri://image"
    )

    @Before
    fun setup() {
        mapper = StartPaintMiniatureVoMapper()
    }

    @Test
    fun `WHEN transform is called THEN vo model is created`() {
        // WHEN
        val result = mapper.transform(miniatureBo)

        // THEN
        assertEquals(miniatureVo.id, result.id)
        assertEquals(miniatureVo.projectId, result.projectId)
        assertEquals(miniatureVo.name, result.name)
        assertEquals(miniatureVo.imageUri, result.imageUri)
    }

    @Test
    fun `WHEN transformReverse is called THEN bo model is created`() {
        // WHEN
        val result = mapper.transformReverse(miniatureVo)

        // THEN
        assertEquals(miniatureBo.id, result.id)
        assertEquals(miniatureBo.projectId, result.projectId)
        assertEquals(miniatureBo.name, result.name)
        assertEquals(miniatureBo.imageUri, result.imageUri)
    }
}