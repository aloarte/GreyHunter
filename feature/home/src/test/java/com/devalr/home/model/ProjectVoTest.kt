package com.devalr.home.model

import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import org.junit.Assert.*
import org.junit.Test

class ProjectVoTest {

    @Test
    fun `GIVEN a project without minis WHEN hasMinis is called THEN returns false`() {
        // GIVEN
        val project = ProjectBo(name = "Warhammer")
        val vo = ProjectVo.ProjectItem(project)

        // WHEN
        val result = vo.hasMinis()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN a project with minis WHEN hasMinis is called THEN returns true`() {
        // GIVEN
        val project = ProjectBo(
            name = "Warhammer",
            minis = listOf(
                MiniatureBo(
                    projectId = 1,
                    name = "Space Marine"
                )
            )
        )
        val vo = ProjectVo.ProjectItem(project)

        // WHEN
        val result = vo.hasMinis()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN a project with all minis completed WHEN hasUnfinishedMinis is called THEN returns false`() {
        // GIVEN
        val project = ProjectBo(
            name = "Warhammer",
            minis = listOf(
                MiniatureBo(projectId = 1, name = "Mini 1", percentage = 1f),
                MiniatureBo(projectId = 1, name = "Mini 2", percentage = 1f)
            )
        )
        val vo = ProjectVo.ProjectItem(project)

        // WHEN
        val result = vo.hasUnfinishedMinis()

        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN a project with at least one unfinished mini WHEN hasUnfinishedMinis is called THEN returns true`() {
        // GIVEN
        val project = ProjectBo(
            name = "Warhammer",
            minis = listOf(
                MiniatureBo(projectId = 1, name = "Mini 1", percentage = 1f),
                MiniatureBo(projectId = 1, name = "Mini 2", percentage = 0.5f)
            )
        )
        val vo = ProjectVo.ProjectItem(project)

        // WHEN
        val result = vo.hasUnfinishedMinis()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN AddProjectItem WHEN methods are called THEN always return false`() {
        // GIVEN
        val vo = ProjectVo.AddProjectItem

        // WHEN
        val hasMinis = vo.hasMinis()
        val hasUnfinished = vo.hasUnfinishedMinis()

        // THEN
        assertFalse(hasMinis)
        assertFalse(hasUnfinished)
    }
}