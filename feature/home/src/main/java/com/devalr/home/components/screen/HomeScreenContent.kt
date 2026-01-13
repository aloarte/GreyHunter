package com.devalr.home.components.screen

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
import com.devalr.home.model.ProjectVo
import com.devalr.home.model.ProjectVo.ProjectItem

@Composable
fun HomeScreenContent(
    projects: List<ProjectVo>,
    lastUpdatedMinis: List<MiniatureBo>,
    almostDoneProjects: List<ProjectBo>,
    onOpenProjectDetail: (Long) -> Unit,
    onOpenMiniatureDetail: (Long) -> Unit,
    onAddProject: () -> Unit,
    onStartPainting: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = spacedBy(10.dp)
    ) {
        item { AppTitle() }
        item {
            ProjectsCarousel(
                projects = projects,
                onProjectClicked = { projectId ->
                    onOpenProjectDetail(projectId)
                },
                onCreateProject = {
                    onAddProject()
                }
            )
        }
        item { GamificationMessage() }
        item {
            LastUpdatedMiniatures(
                miniatures = lastUpdatedMinis,
                onMiniatureClicked = { miniatureId ->
                    onOpenMiniatureDetail(miniatureId)
                }
            )
        }
        item {
            AlmostDoneProjects(
                projects = almostDoneProjects,
                onProjectClicked = { projectId ->
                    onOpenProjectDetail(projectId)
                }
            )
        }
        item {
            StartPaint {
                onStartPainting()
            }
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
                onAddProject = {
                    // Do nothing
                },
                onStartPainting = {
                    // Do nothing
                },
                onOpenProjectDetail = {
                    // Do nothing
                },
                onOpenMiniatureDetail = {
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
                onAddProject = {
                    // Do nothing
                },
                onStartPainting = {
                    // Do nothing
                },
                onOpenProjectDetail = {
                    // Do nothing
                },
                onOpenMiniatureDetail = {
                    // Do nothing
                }

            )
        }
    }
}