package com.devalr.home.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.R
import com.devalr.home.model.GamificationMessageType
import com.devalr.home.model.GamificationMessageType.AlmostDone
import com.devalr.home.model.GamificationMessageType.EmptyProjects
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.GamificationMessageType.ProgressRange

@Composable
fun GamificationMessage(gamificationMessageType: GamificationMessageType) {
    Column(modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp)) {
        GHText(
            text = getGamificationMessage(gamificationMessageType),
            type = TextType.LabelL,
            italic = true
        )
    }

}

@Composable
fun getGamificationMessage(gamificationMessageType: GamificationMessageType) =
    when (gamificationMessageType) {
        EmptyProjects -> stringResource(R.string.label_empty_projects_description)
        is AlmostDone -> stringResource(
            R.string.label_gamification_project,
            gamificationMessageType.projectName
        )

        is ProgressRange -> when (gamificationMessageType.progress) {
            0.2f -> stringResource(R.string.label_gamification_1_20)
            0.5f -> stringResource(R.string.label_gamification_20_50)
            0.7f -> stringResource(R.string.label_gamification_50_70)
            0.9f -> stringResource(R.string.label_gamification_70_90)
            0.99f -> stringResource(R.string.label_gamification_90_99)
            1f -> stringResource(R.string.label_gamification_100)
            else -> ""
        }

        None -> ""
    }

@Preview(showBackground = true)
@Composable
private fun GamificationMessagePreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GamificationMessage(ProgressRange(0.5f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GamificationMessagePreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GamificationMessage(ProgressRange(0.5f))
        }
    }
}