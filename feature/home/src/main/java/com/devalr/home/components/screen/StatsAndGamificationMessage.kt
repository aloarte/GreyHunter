package com.devalr.home.components.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.R
import com.devalr.home.model.GamificationMessageType
import com.devalr.home.model.GamificationMessageType.AlmostDone
import com.devalr.home.model.GamificationMessageType.EmptyProjects
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.GamificationMessageType.ProgressRange
import com.devalr.home.model.ProjectsStats

@Composable
fun StatsAndGamificationMessage(
    gamificationMessageType: GamificationMessageType,
    stats: ProjectsStats
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        MarkedText(
            text = stringResource(R.string.title_stats),
            title = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        GHText(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = getGamificationMessage(gamificationMessageType),
            type = TextType.LabelL,
            italic = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        ProjectTotalStats(stats)
    }
}

@Composable
private fun ProjectTotalStats(stats: ProjectsStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            GHText(
                text = stringResource(R.string.label_stats_total),
                type = TextType.LabelMBold
            )
            GHText(
                text = "${stats.totalProgress}%",
                type = TextType.Title
            )
        }

        if (stats.projectsFinished > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GHText(
                    text = stringResource(R.string.label_stats_projects),
                    type = TextType.LabelMBold
                )
                GHText(
                    modifier = Modifier.padding(end = 5.dp),
                    text = "${stats.projectsFinished}/${stats.totalProjects}",
                    type = TextType.LabelMBold
                )
            }
        }
        if (stats.minisFinished > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GHText(
                    text = stringResource(R.string.label_stats_miniatures),
                    type = TextType.LabelMBold
                )
                GHText(
                    modifier = Modifier.padding(end = 5.dp),
                    text = "${stats.minisFinished}/${stats.totalMinis}",
                    type = TextType.LabelMBold
                )
            }
        }
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
private fun StatsAndGamificationMessagePreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            StatsAndGamificationMessage(
                gamificationMessageType = ProgressRange(0.5f),
                stats = ProjectsStats(
                    totalProgress = 100,
                    totalMinis = 10,
                    totalProjects = 4,
                    projectsFinished = 2,
                    minisFinished = 5
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsAndGamificationMessagePreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            StatsAndGamificationMessage(
                gamificationMessageType = ProgressRange(0.5f),
                stats = ProjectsStats(
                    totalProgress = 100,
                    totalMinis = 10,
                    totalProjects = 4,
                    projectsFinished = 2,
                    minisFinished = 5
                )
            )
        }
    }
}