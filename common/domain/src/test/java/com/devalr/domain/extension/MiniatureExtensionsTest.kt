package com.devalr.domain.extension


import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.enum.MilestoneType.Assembled
import com.devalr.domain.enum.MilestoneType.Base
import com.devalr.domain.enum.MilestoneType.BaseColored
import com.devalr.domain.enum.MilestoneType.Details
import com.devalr.domain.enum.MilestoneType.Primed
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MiniatureExtensionsTest {

    private data class MilestoneTestCase(
        val completion: MiniCompletionBo,
        val targetType: MilestoneType,
        val expectedResult: Boolean
    )

    @Test
    fun `GIVEN a completion with false state WHEN each type is toggled THEN all become true`() {
        // GIVEN
        val initialCompletion = MiniCompletionBo(
            isAssembled = false,
            isPrimed = false,
            isBaseColored = false,
            isDetailed = false,
            baseIsFinished = false
        )

        // WHEN
        val resultAssembled = initialCompletion
            .toggle(Assembled)
            .toggle(Primed)
            .toggle(BaseColored)
            .toggle(Details)
            .toggle(Base)

        // THEN
        assertTrue(resultAssembled.isAssembled)
        assertTrue(resultAssembled.isPrimed)
        assertTrue(resultAssembled.isBaseColored)
        assertTrue(resultAssembled.isDetailed)
        assertTrue(resultAssembled.baseIsFinished)
    }

    @Test
    fun `GIVEN a completion with true state WHEN each type is toggled THEN all become false`() {
        // GIVEN
        val initialCompletion = MiniCompletionBo(
            isAssembled = true,
            isPrimed = true,
            isBaseColored = true,
            isDetailed = true,
            baseIsFinished = true
        )

        // WHEN
        val resultAssembled = initialCompletion
            .toggle(Assembled)
            .toggle(Primed)
            .toggle(BaseColored)
            .toggle(Details)
            .toggle(Base)

        // THEN
        assertFalse(resultAssembled.isAssembled)
        assertFalse(resultAssembled.isPrimed)
        assertFalse(resultAssembled.isBaseColored)
        assertFalse(resultAssembled.isDetailed)
        assertFalse(resultAssembled.baseIsFinished)
    }

    @Test
    fun `GIVEN a miniature with no milestones completed WHEN recalculateProgress is called THEN percentage is 0`() {
        // GIVEN
        val completion = MiniCompletionBo(
            isAssembled = false,
            isPrimed = false,
            isBaseColored = false,
            isDetailed = false,
            baseIsFinished = false
        )
        val miniature = MiniatureBo(projectId = 1, completion = completion, percentage = -1.0f, name = "")

        // WHEN
        val result = miniature.recalculateProgress()

        // THEN
        assertEquals(0.0f, result.percentage, 0.01f)
    }

    @Test
    fun `GIVEN a miniature with all milestones completed WHEN recalculateProgress is called THEN percentage is 1`() {
        // GIVEN
        val completion = MiniCompletionBo(
            isAssembled = true,
            isPrimed = true,
            isBaseColored = true,
            isDetailed = true,
            baseIsFinished = true
        )
        val miniature = MiniatureBo(projectId = 1, completion = completion, percentage = -1.0f, name = "")

        // WHEN
        val result = miniature.recalculateProgress()

        // THEN
        assertEquals(1.0f, result.percentage, 0.01f)
    }

    @Test
    fun `GIVEN various completion states WHEN checking if a milestone can be ENABLED THEN returns expected result based on dependencies`() {
        val testCases = listOf(
            MilestoneTestCase(MiniCompletionBo(isAssembled = false), Assembled, true),
            MilestoneTestCase(MiniCompletionBo(isAssembled = false), Primed, false),
            MilestoneTestCase(MiniCompletionBo(isAssembled = true), Primed, true),
            MilestoneTestCase(MiniCompletionBo(isAssembled = true, isPrimed = false), BaseColored, false),
            MilestoneTestCase(MiniCompletionBo(isAssembled = true, isPrimed = true), BaseColored, true),
            MilestoneTestCase(MiniCompletionBo(isAssembled = true, isPrimed = true, isBaseColored = false), Details, false),
            MilestoneTestCase(MiniCompletionBo(isAssembled = true, isPrimed = true, isBaseColored = true), Details, true),
            MilestoneTestCase(MiniCompletionBo(), Base, true)
        )

        testCases.forEach { testCase ->
            // WHEN
            val result = testCase.completion.isMilestoneAchievable(testCase.targetType, enable = true)

            // THEN
            if (result != testCase.expectedResult) {
                throw AssertionError("Failed Test: Expected ${testCase.expectedResult} but got $result")
            }
        }
    }

    @Test
    fun `GIVEN various completion states WHEN checking if a milestone can be DISABLED THEN returns expected result based on forward dependencies`() {
        val testCases = listOf(
            MilestoneTestCase(MiniCompletionBo(isAssembled = true, isPrimed = true), Assembled, false),
            MilestoneTestCase(MiniCompletionBo(isAssembled = true, isPrimed = false), Assembled, true),
            MilestoneTestCase(MiniCompletionBo(isPrimed = true, isBaseColored = true), Primed, false),
            MilestoneTestCase(MiniCompletionBo(isPrimed = true, isBaseColored = false), Primed, true),
            MilestoneTestCase(MiniCompletionBo(isBaseColored = true, isDetailed = true), BaseColored, false),
            MilestoneTestCase(MiniCompletionBo(isBaseColored = true, isDetailed = false), BaseColored, true),
            MilestoneTestCase(MiniCompletionBo(isDetailed = true), Details, true),
            MilestoneTestCase(MiniCompletionBo(baseIsFinished = true), Base, true)
        )

        testCases.forEach { testCase ->
            // WHEN
            val result = testCase.completion.isMilestoneAchievable(testCase.targetType, enable = false)

            // THEN
            if (result != testCase.expectedResult) {
                throw AssertionError("Failed Test: Expected ${testCase.expectedResult} but got $result")
            }
        }
    }
}