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
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.components.detail.TopButtons
import com.devalr.framework.components.progress.GHProgressBar
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun ProjectDetailScreenContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    project: ProjectBo,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit
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
            borderRadius = 0.dp,
            imageUri = project.imageUri,
            size = 160.dp
        )

        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            onBackPressed = onBackPressed,
            onEditPressed = onEditPressed
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
                        item { GHText(text = project.name, type = TextType.Title) }
                        project.description?.let { description ->
                            item {
                                GHText(text = description, type = TextType.Description)
                            }
                        }

                        item { GHProgressBar(percentage = project.progress) }
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
            project = ProjectBo(name = "Project Name"),
            onNavigateToMiniature = {
                // Do nothing
            },
            onCreateMiniature = {
                // Do nothing
            },
            onBackPressed = {
                // Do nothing
            },
            onEditPressed = {
                // Do nothing
            }
        )
    }
}