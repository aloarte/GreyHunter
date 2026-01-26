package com.devalr.framework.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun RadioSelectorBottomSheetContent(
    label: String,
    selectedIndex: Int,
    optionList: List<String>,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        GHText(text = label, type = TextType.Title)
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            optionList.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = index == selectedIndex,
                        onClick = { onOptionSelected(index) }
                    )
                    GHText(text = option, type = TextType.Description)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioSelectorBottomSheetContentPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            RadioSelectorBottomSheetContent(
                label = "Theme",
                optionList = listOf("Dark", "Light", "System"),
                selectedIndex = 1,
                onOptionSelected = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioSelectorBottomSheetContentPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            RadioSelectorBottomSheetContent(
                label = "Theme",
                optionList = listOf("Dark", "Light", "System"),
                selectedIndex = 2,
                onOptionSelected = {
                    // Do nothing
                }
            )
        }
    }
}