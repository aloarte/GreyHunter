package com.devalr.framework.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun TextWithSectionsBottomSheetContent(
    title: String,
    content: Map<String, String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        GHText(text = title, type = TextType.Title)
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(content.entries.toList()) { entry ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    GHText(text = entry.key, type = TextType.Title)
                    GHText(text = entry.value, type = TextType.Description)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextWithSectionsBottomSheetContentPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TextWithSectionsBottomSheetContent(
                title = "Change log",
                content = mapOf(
                    "v26.1.0" to "This is a change log\n · New feature 1\n · New feature 2",
                    "v26.1.1" to "Second change log\n · New feature 3\n · New feature 4"
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextWithSectionsBottomSheetContentPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TextWithSectionsBottomSheetContent(
                title = "Change log",
                content = mapOf(
                    "v26.1.0" to "This is a change log\n · New feature 1\n · New feature 2",
                    "v26.1.1" to "Second change log\n · New feature 3\n · New feature 4"
                )
            )
        }
    }
}