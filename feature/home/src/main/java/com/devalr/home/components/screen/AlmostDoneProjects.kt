package com.devalr.home.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.devalr.framework.HOME_NEARLY_COMPLETED_PROJECTS
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.components.row.ResponsiveRow
import com.devalr.framework.enum.CardType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.R
import com.devalr.home.components.cards.ProjectCard

@Composable
fun AlmostDoneProjects(
    projects: List<ProjectBo>,
    onOpenProject: (Long) -> Unit
) {
    if (projects.isNotEmpty()) {
        Column(modifier = Modifier.padding(25.dp)) {
            MarkedText(
                testTag = HOME_NEARLY_COMPLETED_PROJECTS,
                text = stringResource(R.string.title_almost_done_projects),
                title = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            ResponsiveRow(items = projects) { project ->
                ProjectCard(
                    project = project,
                    cardType = CardType.Home,
                    onOpenProject = onOpenProject
                )
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
                onOpenProject = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlmostDoneProjectsPreviewLightModeOneProject() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AlmostDoneProjects(
                projects = listOf(hierotekCircleProject),
                onOpenProject = {
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
                onOpenProject = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlmostDoneProjectsPreviewDarkModeZeroProjects() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AlmostDoneProjects(
                projects = emptyList(),
                onOpenProject = {
                    // Do nothing
                }
            )
        }
    }
}
