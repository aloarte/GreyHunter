package com.devalr.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.bottomsheet.TextWithSectionsBottomSheetContent
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfo(appVersion: String) {

    var displayChangeLog by remember { mutableStateOf(false) }
    if (displayChangeLog) {
        ModalBottomSheet(onDismissRequest = { displayChangeLog = false }) {
            TextWithSectionsBottomSheetContent(
                title = stringResource(R.string.label_settings_changelog),
                content = getChangeLogContent()
            )
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {
        GHText(text = stringResource(R.string.label_title_app_info_settings), type = TextType.Title)
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
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GHText(
                        text = stringResource(R.string.label_settings_version, appVersion),
                        type = TextType.LabelMBold
                    )
                }
                SettingsItem(
                    iconPainter = painterResource(com.devalr.framework.R.drawable.ic_import),
                    label = stringResource(R.string.label_settings_changelog),
                    onOpenSettings = { displayChangeLog = true }
                )
            }
        }
    }
}

@Composable
private fun getChangeLogContent(): Map<String, String> = mapOf(
    "26.1.1" to stringResource(R.string.changelog_26_1_0),
)


@Preview(showBackground = true)
@Composable
fun AppInfoPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppInfo(appVersion = "26.1.1")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppInfoPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppInfo(appVersion = "26.1.1")
        }
    }
}
