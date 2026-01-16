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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.carousel.HorizontalCarousel
import com.devalr.framework.components.cards.AddCard
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.enum.CardType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.components.cards.ProjectCard
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProject
import com.devalr.home.model.ProjectVo.ProjectItem

@Composable
fun ProjectsCarousel(
    projects: List<ProjectVo>,
    onProjectClicked: (Long) -> Unit,
    onCreateProject: () -> Unit
) {
    Column {
        MarkedText(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Projects",
            title = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalCarousel(items = projects, dots = true) { item ->
            when (item) {
                is ProjectItem -> ProjectCard(
                    project = item.project,
                    onProjectClicked = onProjectClicked
                )

                is AddProject -> AddCard(
                    type = CardType.Project,
                    onCreate = onCreateProject
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProjectsCarousel(
                projects = listOf(
                    ProjectItem(hierotekCircleProject),
                    ProjectItem(stormlightArchiveProject),
                    AddProject
                ),
                onCreateProject = {
                    // Do nothing
                },
                onProjectClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProjectsCarousel(
                projects = listOf(
                    ProjectItem(hierotekCircleProject),
                    ProjectItem(stormlightArchiveProject),
                    AddProject
                ),
                onCreateProject = {
                    // Do nothing
                },
                onProjectClicked = {
                    // Do nothing
                }
            )
        }
    }
}
