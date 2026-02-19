package com.devalr.framework

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.devalr.framework.components.add.AddItemDescription
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddItemDescriptionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenAddItemDescription_whenUserTypesLessThan500Characters_thenOnChangeIsTriggered() {
        // GIVEN
        var updatedText: String? = null

        composeTestRule.setContent {
            AddItemDescription(
                description = "",
                label = "Description",
                onChangeDescription = { updatedText = it }
            )
        }

        // WHEN
        composeTestRule
            .onNodeWithText("Description")
            .performTextInput("Hello World")

        // THEN
        TestCase.assertEquals("Hello World", updatedText)
    }

    @Test
    fun givenAddItemDescription_whenUserTypesMoreThan500Characters_thenOnChangeIsNotTriggered() {
        // GIVEN
        var updatedText: String? = null
        val longText = "a".repeat(501)

        composeTestRule.setContent {
            AddItemDescription(
                description = "",
                label = "Description",
                onChangeDescription = { updatedText = it }
            )
        }

        // WHEN
        composeTestRule
            .onNodeWithText("Description")
            .performTextInput(longText)

        // THEN
        TestCase.assertNull(updatedText)
    }
}