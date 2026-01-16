package com.devalr.framework.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.R
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun TopButtons(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onEditPressed: (() -> Unit)? = null,
    onDeletePressed: (() -> Unit)? = null
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        GHIconButton(icon = Icons.Default.ArrowBack, onButtonClicked = onBackPressed)
        Row {
            if (onEditPressed != null) {
                Spacer(modifier = Modifier.width(10.dp))
                GHIconButton(
                    painter = painterResource(R.drawable.ic_edit),
                    onButtonClicked = onEditPressed
                )
            }
            if (onDeletePressed != null) {
                Spacer(modifier = Modifier.width(10.dp))
                GHIconButton(
                    painter = painterResource(R.drawable.ic_delete),
                    onButtonClicked = onDeletePressed
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopButtonsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        TopButtons(
            onBackPressed = {
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            },
            onDeletePressed = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopButtonsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        TopButtons(
            onBackPressed = {
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            },
            onDeletePressed = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopButtonsPreviewOnlyBack() {
    GreyHunterTheme(darkTheme = false) {
        TopButtons(
            onBackPressed = {
                // Do nothing
            }
        )
    }
}