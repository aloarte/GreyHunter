package com.devalr.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.bottomsheet.ConfirmBottomSheetContent
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSettings(
    onImportProjects: () -> Unit,
    onExportProjects: () -> Unit
) {
    var displayConfirmExport by remember { mutableStateOf(false) }
    if (displayConfirmExport) {
        ModalBottomSheet(onDismissRequest = { displayConfirmExport = false }) {
            ConfirmBottomSheetContent(
                description = stringResource(R.string.bottom_sheet_confirm_export),
                okButtonText = stringResource(R.string.bottom_sheet_confirm_export_btn),
                onConfirmDelete = {
                    onExportProjects()
                    displayConfirmExport = false
                },
                onDeny = {
                    displayConfirmExport = false
                }
            )
        }
    }
    Column(modifier = Modifier.padding(20.dp)) {
        GHText(text = stringResource(R.string.label_title_data_settings), type = TextType.Title)
        Spacer(modifier = Modifier.height(10.dp))
        GHText(
            text = stringResource(R.string.label_description_data_settings),
            type = TextType.Description
        )
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = spacedBy(5.dp)
            ) {
                SettingsItem(
                    iconPainter = painterResource(com.devalr.framework.R.drawable.ic_import),
                    label = stringResource(R.string.label_settings_import),
                    onOpenSettings = onImportProjects
                )
                SettingsItem(
                    iconPainter = painterResource(com.devalr.framework.R.drawable.ic_export),
                    label = stringResource(R.string.label_settings_export),
                    onOpenSettings = { displayConfirmExport = true }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataSettingsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DataSettings(
                onImportProjects = {
                    // Do nothing
                },
                onExportProjects = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataSettingsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DataSettings(
                onImportProjects = {
                    // Do nothing
                },
                onExportProjects = {
                    // Do nothing
                }
            )
        }
    }
}
