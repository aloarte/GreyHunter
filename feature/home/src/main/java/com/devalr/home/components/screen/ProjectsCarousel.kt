package com.devalr.home.components.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.GHText
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.framework.components.TextType
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
    Column {
        GHText(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Projects",
            type = TextType.Title
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
