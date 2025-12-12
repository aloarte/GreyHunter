package com.devalr.home.components.screen

import androidx.compose.runtime.Composable
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.home.components.cards.ProjectCard

@Composable
fun ProjectsCarousel(projects: List<ProjectBo>, onProjectClicked: (Long) -> Unit) {
    HorizontalCarousel(items = projects) { item ->
        ProjectCard(projectBo = item, onProjectClicked = onProjectClicked)

    }

}
