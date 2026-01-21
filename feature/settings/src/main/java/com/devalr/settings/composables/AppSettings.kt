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
import com.devalr.domain.enum.ThemeType
import com.devalr.framework.components.bottomsheet.RadioSelectorBottomSheetContent
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettings(
    currentThemeType: ThemeType,
    onThemeClicked: (ThemeType) -> Unit,
    onChangeColorClicked: () -> Unit,
) {

    var displayThemeSelector by remember { mutableStateOf(false) }

    if (displayThemeSelector) {
        val themeOptions =
            listOf(ThemeType.Light, ThemeType.Dark, ThemeType.System)
        ModalBottomSheet(onDismissRequest = { displayThemeSelector = false }) {
            RadioSelectorBottomSheetContent(
                label = stringResource(R.string.label_settings_theme),
                optionList = getThemeNameList(themeOptions),
                selectedIndex = themeOptions.indexOf(currentThemeType),
                onOptionClicked = { themeOption ->
                    onThemeClicked(themeOptions[themeOption])
                    displayThemeSelector = false
                }
            )
        }

    }

    Column(modifier = Modifier.padding(20.dp)) {
        GHText(text = stringResource(R.string.label_title_app_settings), type = TextType.Title)
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
                    iconPainter = painterResource(com.devalr.framework.R.drawable.ic_dark_mode),
                    label = stringResource(R.string.label_settings_theme),
                    currentValue = currentThemeType.name,
                    onSettingsItemClicked = {
                        displayThemeSelector = true
                    }
                )
                SettingsItem(
                    iconPainter = painterResource(com.devalr.framework.R.drawable.ic_colors),
                    label = stringResource(R.string.label_settings_colors),
                    currentValue = "Monochromatic",
                    onSettingsItemClicked = { }
                )
            }
        }

    }
}

@Composable
private fun getThemeNameList(themeOptions: List<ThemeType>): List<String> =
    themeOptions.map {
        when (it) {
            ThemeType.Light -> stringResource(R.string.label_settings_theme_light)
            ThemeType.Dark -> stringResource(R.string.label_settings_theme_dark)
            ThemeType.System -> stringResource(R.string.label_settings_theme_system)
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
                currentThemeType = ThemeType.Light,
                onThemeClicked = {
                    // Do nothing
                },
                onChangeColorClicked = {
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
                currentThemeType = ThemeType.System,
                onThemeClicked = {
                    // Do nothing
                },
                onChangeColorClicked = {
                    // Do nothing
                }
            )
        }
    }
}
