package com.devalr.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.helpers.alethi
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.immortal
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.components.anim.EmptyProjects
import com.devalr.home.components.screen.AlmostDoneProjects
import com.devalr.home.components.screen.AppTitle
import com.devalr.home.components.screen.LastUpdatedMiniatures
import com.devalr.home.components.screen.ProjectsCarousel
import com.devalr.home.components.screen.StatsAndGamificationMessage
import com.devalr.home.model.GamificationMessageType
import com.devalr.home.model.GamificationMessageType.EmptyProjects
import com.devalr.home.model.GamificationMessageType.None
import com.devalr.home.model.GamificationMessageType.ProgressRange
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.AddProjectItem
import com.devalr.home.model.ProjectVo.ProjectItem
import com.devalr.home.model.ProjectsStats

@Composable
fun HomeScreenContent(
    projects: List<ProjectVo>,
    lastUpdatedMinis: List<MiniatureBo>,
    almostDoneProjects: List<ProjectBo>,
    gamificationMessage: GamificationMessageType,
    stats: ProjectsStats,
    onOpenProjectDetail: (Long) -> Unit,
    onOpenMiniatureDetail: (Long) -> Unit,
    onAddProject: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = spacedBy(10.dp)
    ) {
        item { AppTitle(onNavigateToSettings = onNavigateToSettings) }
        item {
            ProjectsCarousel(
                projects = projects,
                onOpenProject = { projectId ->
                    onOpenProjectDetail(projectId)
                },
                onCreateProject = {
                    onAddProject()
                }
            )
        }
        item {
            if (projects.size > 1 && projects.any { it.hasMinis() } && gamificationMessage != None) {
                StatsAndGamificationMessage(gamificationMessage, stats)
            } else {
                EmptyProjects(projects.size > 1)
            }
        }
        item {
            LastUpdatedMiniatures(
                miniatures = lastUpdatedMinis,
                onOpenMiniature = { miniatureId ->
                    onOpenMiniatureDetail(miniatureId)
                }
            )
        }
        item {
            AlmostDoneProjects(
                projects = almostDoneProjects,
                onOpenProject = { projectId ->
                    onOpenProjectDetail(projectId)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeScreenContent(
                projects = listOf(
                    ProjectItem(hierotekCircleProject),
                    ProjectItem(stormlightArchiveProject)
                ),
                lastUpdatedMinis = listOf(immortal, alethi),
                almostDoneProjects = listOf(hierotekCircleProject, stormlightArchiveProject),
                gamificationMessage = ProgressRange(0.4f),
                stats = ProjectsStats(),
                onAddProject = {
                    // Do nothing
                },
                onOpenProjectDetail = {
                    // Do nothing
                },
                onOpenMiniatureDetail = {
                    // Do nothing
                },
                onNavigateToSettings = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeScreenContent(
                projects = listOf(
                    ProjectItem(hierotekCircleProject),
                    ProjectItem(stormlightArchiveProject)
                ),
                lastUpdatedMinis = listOf(immortal, alethi),
                almostDoneProjects = listOf(hierotekCircleProject, stormlightArchiveProject),
                gamificationMessage = ProgressRange(0.4f),
                stats = ProjectsStats(),
                onAddProject = {
                    // Do nothing
                },
                onOpenProjectDetail = {
                    // Do nothing
                },
                onOpenMiniatureDetail = {
                    // Do nothing
                },
                onNavigateToSettings = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentNoProjectsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeScreenContent(
                projects = listOf(AddProjectItem),
                lastUpdatedMinis = emptyList(),
                almostDoneProjects = emptyList(),
                gamificationMessage = EmptyProjects,
                stats = ProjectsStats(),
                onAddProject = {
                    // Do nothing
                },
                onOpenProjectDetail = {
                    // Do nothing
                },
                onOpenMiniatureDetail = {
                    // Do nothing
                },
                onNavigateToSettings = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentNoProjectsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeScreenContent(
                projects = listOf(AddProjectItem),
                lastUpdatedMinis = emptyList(),
                almostDoneProjects = emptyList(),
                gamificationMessage = EmptyProjects,
                stats = ProjectsStats(),
                onAddProject = {
                    // Do nothing
                },
                onOpenProjectDetail = {
                    // Do nothing
                },
                onOpenMiniatureDetail = {
                    // Do nothing
                },
                onNavigateToSettings = {
                    // Do nothing
                }
            )
        }
    }
}