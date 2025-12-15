package com.devalr.home.components.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.framework.components.cards.AddCard
import com.devalr.framework.enum.CardType
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
    HorizontalCarousel(items = projects) { item ->
        when (item) {
            is ProjectItem -> ProjectCard(
                projectBo = item.project,
                onProjectClicked = onProjectClicked
            )

            is AddProject -> AddCard(
                type = CardType.Project,
                onCreate = onCreateProject
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselPreview() {
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
