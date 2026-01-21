package com.devalr.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme


@Composable
fun SettingsItem(
    iconPainter: Painter,
    label: String,
    currentValue: String? = null,
    onSettingsItemClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSettingsItemClicked() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(15.dp),
                painter = iconPainter,
                contentDescription = "Start Painting",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(10.dp))
            GHText(text = label, type = TextType.LabelSBold)
        }
        currentValue?.let {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GHText(text = label, type = TextType.LabelSBold)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = "Start Painting",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsItem(
                iconPainter = painterResource(com.devalr.framework.R.drawable.ic_paint),
                label = "Appearance",
                currentValue = "System",
                onSettingsItemClicked = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsItem(
                iconPainter = painterResource(com.devalr.framework.R.drawable.ic_paint),
                label = "Appearance",
                currentValue = "System",
                onSettingsItemClicked = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemNoCurrentValuePreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsItem(
                iconPainter = painterResource(com.devalr.framework.R.drawable.ic_gallery),
                label = "Export",
                onSettingsItemClicked = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemNoCurrentValuePreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SettingsItem(
                iconPainter = painterResource(com.devalr.framework.R.drawable.ic_gallery),
                label = "Export",
                onSettingsItemClicked = { }
            )
        }
    }
}

