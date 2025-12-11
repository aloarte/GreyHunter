package com.devalr.home.components.screen

import androidx.compose.runtime.Composable
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.home.components.cards.ProjectCard

@Composable
fun ProjectsCarousel() {
    HorizontalCarousel(
        items = listOf(
            ProjectBo(name = "Project 1"),
            ProjectBo(name = "Project 1")
        )
    ) { item ->
        ProjectCard(projectBo = item)

    }

}
