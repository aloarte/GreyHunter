package com.devalr.framework.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.FRAMEWORK_BOTTOM_SHEET_POSITIVE_BUTTON
import com.devalr.framework.R
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun ConfirmBottomSheetContent(
    description: String,
    okButtonText: String,
    onConfirmDelete: () -> Unit,
    onDeny: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GHText(
            text = description,
            type = TextType.Description
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GHButton(
                modifier = Modifier
                    .weight(1f)
                    .semantics { contentDescription = FRAMEWORK_BOTTOM_SHEET_POSITIVE_BUTTON },
                text = okButtonText,
                onClick = onConfirmDelete
            )

            GHButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.btn_cancel),
                onClick = onDeny
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBottomSheetContentPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ConfirmBottomSheetContent(
                description = "Are you sure that you want to delete this project?",
                okButtonText = "Delete",
                onConfirmDelete = {
                    // Do nothing
                },
                onDeny = {
                    // Do nothing
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBottomSheetContentPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ConfirmBottomSheetContent(
                description = "Are you sure that you want to delete this project?",
                okButtonText = "Delete",
                onConfirmDelete = {
                    // Do nothing
                },
                onDeny = {
                    // Do nothing
                }
            )
        }
    }
}