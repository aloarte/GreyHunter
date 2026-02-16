package com.devalr.startpainting.mapper

import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class StartPaintProjectVoMapperTest {

    private val miniatureMapper: Mapper<MiniatureBo, StartPaintMiniatureVo> = mockk()

    private lateinit var mapper: StartPaintProjectVoMapper

    private val miniatureBo = MiniatureBo(id = 12L, projectId = 1L, name = "mini")
    private val miniatureVo = StartPaintMiniatureVo(id = 12L, projectId = 1L, name = "mini")
    private val projectBo = ProjectBo(
        id = 1L,
        name = "Project",
        minis = listOf(miniatureBo)
    )
    private val projectVo = StartPaintProjectVo(
        id =  1L,
        name = "Project",
        minis = listOf(miniatureVo)
    )

    @Before
    fun setup() {
        mapper = StartPaintProjectVoMapper(miniatureMapper)
    }

    @Test
    fun `WHEN transform project THEN vo model is created`() {
        // GIVEN
        every { miniatureMapper.transform(miniatureBo) } returns miniatureVo

        // WHEN
        val result = mapper.transform(projectBo)

        // THEN
        assertEquals(projectVo.id, result.id)
        assertEquals(projectVo.name, result.name)
        assertEquals(listOf(miniatureVo), result.minis)
        verify(exactly = 1) { miniatureMapper.transform(miniatureBo) }
    }

    @Test
    fun `WHEN transformReverse from vo is called THEN bo model is created`() {
        // GIVEN
        every { miniatureMapper.transformReverse(miniatureVo) } returns miniatureBo

        // WHEN
        val result = mapper.transformReverse(projectVo)

        // THEN
        assertEquals(projectBo.id, result.id)
        assertEquals(projectBo.name, result.name)
        assertEquals(listOf(miniatureBo), result.minis)
        verify(exactly = 1) { miniatureMapper.transformReverse(miniatureVo) }
    }
}