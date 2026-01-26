package com.devalr.projectdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.gh.GHImage
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun ProjectDetailScreenContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    project: ProjectBo,
    onNavigateToMiniature: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    onCreateMiniature: () -> Unit,
    onEditProject: () -> Unit,
    onDeleteProject: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {

        GHImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            showMessage = true,
            borderRadius = 0.dp,
            imageUri = project.imageUri,
            size = 160.dp
        )

        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            onNavigateBack = onNavigateBack,
            onEdit = onEditProject,
            onDelete = onDeleteProject
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {
            item { Spacer(modifier = Modifier.height(200.dp)) }
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .clip(
                            RoundedCornerShape(
                                topStart = 24.dp,
                                topEnd = 24.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp)
                ) {

                    LazyColumn {
                        item { ProjectInfo(project) }
                        item {
                            ProjectMiniatures(
                                miniatures = project.minis,
                                onNavigateToMiniature = onNavigateToMiniature,
                                onCreateMiniature = onCreateMiniature
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProjectDetailScreenContentPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        ProjectDetailScreenContent(
            project = ProjectBo(
                name = "Project Name",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue neque at diam sollicitudin, ac aliquet felis imperdiet. In a convallis felis, ac elementum diam. Morbi pretium ante sed tellus tincidunt interdum. Suspendisse at sem congue, pretium nibh quis, elementum arcu.",
                progress = 0.3f,
                minis = listOf(
                    MiniatureBo(id = 1, name = "Mini 1", projectId = 1, percentage = 1.0f),
                    MiniatureBo(id = 2, name = "Mini 2", projectId = 1, percentage = 0.5f),
                    MiniatureBo(id = 3, name = "Mini 2", projectId = 1, percentage = 0.2f),
                    MiniatureBo(id = 3, name = "Mini 2", projectId = 1, percentage = 0.0f)
                )
            ),
            onNavigateToMiniature = {
                // Do nothing
            },
            onCreateMiniature = {
                // Do nothing
            },
            onNavigateBack = {
                // Do nothing
            },
            onEditProject = {
                // Do nothing
            },
            onDeleteProject = {
                // Do nothing
            }
        )
    }
}