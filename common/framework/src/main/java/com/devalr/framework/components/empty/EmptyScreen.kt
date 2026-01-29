package com.devalr.framework.components.empty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun EmptyScreen(onNavigateBack: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GHText(text = "Empty Screen", type = TextType.Title)
            Spacer(modifier = Modifier.height(10.dp))
            GHText(
                text = "Oops sorry. We couldn't load the screen. Please, go back and try again.",
                type = TextType.Description
            )
            Spacer(modifier = Modifier.height(20.dp))
            onNavigateBack?.let {
                GHButton(text = "Return to previous screen", onClick = onNavigateBack)
            } ?: run {
                GHText(
                    text = "Please, close the app and load it from scratch.",
                    type = TextType.Description,
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun EmptyScreenPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            EmptyScreen {
                // Do nothing
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyScreenPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            EmptyScreen {
                // Do nothing
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyScreenWithoutButtonPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            EmptyScreen()
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyScreenWithoutButtonPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            EmptyScreen()
        }
    }
}