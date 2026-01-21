package com.devalr.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.enum.AppearanceType
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun AppSettings(
    currentAppearanceType: AppearanceType,
    onAppearanceClicked: (AppearanceType) -> Unit,
    onLanguageClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(5.dp)) {
        GHText(text = "App settings", type = TextType.Title)
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp),
            verticalArrangement = spacedBy(5.dp)
        ) {
            SettingsItem(
                iconPainter = painterResource(com.devalr.framework.R.drawable.ic_paint),
                label = "Appearance",
                currentValue = currentAppearanceType.name,
                onSettingsItemClicked = { }
            )
            SettingsItem(
                iconPainter = painterResource(com.devalr.framework.R.drawable.ic_camera),
                label = "Progress colours",
                currentValue = "Monochromatic",
                onSettingsItemClicked = { }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppSettingsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppSettings(
                currentAppearanceType = AppearanceType.Light,
                onAppearanceClicked = {
                    // Do nothing
                },
                onLanguageClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppSettingsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppSettings(
                currentAppearanceType = AppearanceType.Dark,
                onAppearanceClicked = {
                    // Do nothing
                },
                onLanguageClicked = {
                    // Do nothing
                }
            )
        }
    }
}
