package com.devalr.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CompletionProportionsBoTest {

    @Test
    fun `GIVEN no parameters WHEN CompletionProportionsBo is created THEN default values are assigned`() {
        // WHEN
        val proportions = CompletionProportionsBo()

        // THEN
        assertEquals(0.2f, proportions.assembled)
        assertEquals(0.2f, proportions.primed)
        assertEquals(0.3f, proportions.baseColored)
        assertEquals(0.2f, proportions.detailed)
        assertEquals(0.1f, proportions.base)
        val total = proportions.assembled +
                proportions.primed +
                proportions.baseColored +
                proportions.detailed +
                proportions.base

        assertEquals(1.0f, total)
    }

    @Test
    fun `GIVEN default proportions WHEN detailed is updated using copy THEN only detailed value changes`() {
        // GIVEN
        val original = CompletionProportionsBo()

        // WHEN
        val updated = original.copy(detailed = 0.5f)

        // THEN
        assertEquals(0.5f, updated.detailed)
        assertEquals(original.assembled, updated.assembled)
        assertEquals(original.primed, updated.primed)
        assertEquals(original.baseColored, updated.baseColored)
        assertEquals(original.base, updated.base)
    }
}