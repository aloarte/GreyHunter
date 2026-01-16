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
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.enum.CardType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.R
import com.devalr.home.components.cards.ProjectCard

@Composable
fun AlmostDoneProjects(
    projects: List<ProjectBo>,
    onProjectClicked: (Long) -> Unit
) {
    if (projects.isNotEmpty()) {
        Column(modifier = Modifier.padding(25.dp)) {
            MarkedText(text = stringResource(R.string.title_almost_done_projects))
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                projects.forEach {
                    ProjectCard(
                        project = it,
                        cardType = CardType.Home,
                        onProjectClicked = onProjectClicked
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlmostDoneProjectsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AlmostDoneProjects(
                projects = listOf(hierotekCircleProject, stormlightArchiveProject),
                onProjectClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlmostDoneProjectsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AlmostDoneProjects(
                projects = listOf(hierotekCircleProject, stormlightArchiveProject),
                onProjectClicked = {
                    // Do nothing
                }
            )
        }
    }
}
