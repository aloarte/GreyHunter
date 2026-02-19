package com.devalr.framework

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun `GIVEN empty string WHEN limitSize is called THEN empty string is returned`() {
        // GIVEN
        val emptyString = ""

        // WHEN
        val result = emptyString.limitSize(20)

        // THEN
        assertEquals(emptyString, result)
    }

    @Test
    fun `GIVEN not empty string WHEN limitSize is called with a higher size THEN string is returned`() {
        // GIVEN
        val stringThatFits = "String that fits"

        // WHEN
        val result = stringThatFits.limitSize(20)

        // THEN
        assertEquals(stringThatFits, result)
    }

    @Test
    fun `GIVEN not empty string WHEN limitSize is called with a lower size THEN string is returned`() {
        // GIVEN
        val stringThatFits = "String that doesnt fits"

        // WHEN
        val result = stringThatFits.limitSize(2)

        // THEN
        assertEquals("St...", result)
    }
}