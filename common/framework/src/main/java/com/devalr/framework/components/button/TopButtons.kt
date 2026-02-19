package com.devalr.framework.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.FRAMEWORK_TOP_BUTTON_DELETE
import com.devalr.framework.R
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun TopButtons(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        GHIconButton(
            icon = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
            onButtonClicked = onNavigateBack
        )
        Row {
            if (onEdit != null) {
                Spacer(modifier = Modifier.width(10.dp))
                GHIconButton(
                    painter = painterResource(R.drawable.ic_edit),
                    onButtonClicked = onEdit
                )
            }
            if (onDelete != null) {
                Spacer(modifier = Modifier.width(10.dp))
                GHIconButton(
                    modifier = Modifier.semantics {
                        contentDescription = FRAMEWORK_TOP_BUTTON_DELETE
                    },
                    painter = painterResource(R.drawable.ic_delete),
                    onButtonClicked = onDelete
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
            onNavigateBack = {
                // Do nothing
            },
            onEdit = {
                // Do nothing
            },
            onDelete = {
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
            onNavigateBack = {
                // Do nothing
            },
            onEdit = {
                // Do nothing
            },
            onDelete = {
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
            onNavigateBack = {
                // Do nothing
            }
        )
    }
}