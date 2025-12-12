package com.devalr.home.components.screen

import androidx.compose.runtime.Composable
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.home.components.cards.ProjectCard

@Composable
fun ProjectsCarousel() {
    HorizontalCarousel(
        items = listOf(
            hierotekCircleProject,
            hierotekCircleProject,
            stormlightArchiveProject,
            hierotekCircleProject
        )
    ) { item ->
        ProjectCard(projectBo = item)

    }

}
