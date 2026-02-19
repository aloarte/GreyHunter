package com.devalr.settings.components

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
import com.devalr.domain.enums.ProgressColorType
import com.devalr.domain.enums.ThemeType
import com.devalr.framework.components.bottomsheet.RadioSelectorBottomSheetContent
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettings(
    currentThemeType: ThemeType,
    currentProgressColorType: ProgressColorType,
    onChangeTheme: (ThemeType) -> Unit,
    onChangeProgressColor: (ProgressColorType) -> Unit,
) {

    var displayThemeSelector by remember { mutableStateOf(false) }
    var displayProgressColorSelector by remember { mutableStateOf(false) }

    if (displayThemeSelector) {
        val themeOptions =
            listOf(ThemeType.Light, ThemeType.Dark, ThemeType.System)
        ModalBottomSheet(onDismissRequest = { displayThemeSelector = false }) {
            RadioSelectorBottomSheetContent(
                label = stringResource(R.string.label_settings_theme),
                optionList = getThemeNameList(themeOptions),
                selectedIndex = themeOptions.indexOf(currentThemeType),
                onOptionSelected = { themeOption ->
                    onChangeTheme(themeOptions[themeOption])
                    displayThemeSelector = false
                }
            )
        }
    }
    if (displayProgressColorSelector) {
        val progressColorOptions =
            listOf(
                ProgressColorType.Brand,
                ProgressColorType.TrafficLight
            )
        ModalBottomSheet(onDismissRequest = { displayProgressColorSelector = false }) {
            RadioSelectorBottomSheetContent(
                label = stringResource(R.string.label_settings_theme),
                optionList = getProgressColorNameList(progressColorOptions),
                selectedIndex = progressColorOptions.indexOf(currentProgressColorType),
                onOptionSelected = { themeOption ->
                    onChangeProgressColor(progressColorOptions[themeOption])
                    displayProgressColorSelector = false
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
                    currentValue = getThemeName(currentThemeType),
                    onOpenSettings = { displayThemeSelector = true }
                )
                SettingsItem(
                    iconPainter = painterResource(com.devalr.framework.R.drawable.ic_colors),
                    label = stringResource(R.string.label_settings_colors),
                    currentValue = getProgressColorName(currentProgressColorType),
                    onOpenSettings = { displayProgressColorSelector = true }
                )
            }
        }
    }
}

@Composable
private fun getThemeNameList(themeOptions: List<ThemeType>): List<String> =
    themeOptions.map { getThemeName(it) }

@Composable
private fun getThemeName(themeType: ThemeType): String = when (themeType) {
    ThemeType.Light -> stringResource(R.string.label_settings_theme_light)
    ThemeType.Dark -> stringResource(R.string.label_settings_theme_dark)
    ThemeType.System -> stringResource(R.string.label_settings_theme_system)
}

@Composable
private fun getProgressColorNameList(colorOptions: List<ProgressColorType>): List<String> =
    colorOptions.map { getProgressColorName(it) }

@Composable
private fun getProgressColorName(colorType: ProgressColorType): String = when (colorType) {
    ProgressColorType.Brand -> stringResource(R.string.label_settings_colors_brand)
    ProgressColorType.TrafficLight -> stringResource(R.string.label_settings_colors_traffic)
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
                currentProgressColorType = ProgressColorType.TrafficLight,
                onChangeTheme = {
                    // Do nothing
                },
                onChangeProgressColor = {
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
                currentProgressColorType = ProgressColorType.TrafficLight,
                onChangeTheme = {
                    // Do nothing
                },
                onChangeProgressColor = {
                    // Do nothing
                }
            )
        }
    }
}
