package com.devalr.projectdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHTab
import com.devalr.framework.components.GHText
import com.devalr.framework.components.LoadingIndicator
import com.devalr.framework.components.TextType
import com.devalr.framework.components.progress.GHProgressBar
import com.devalr.framework.components.progress.VerticalProgress
import com.devalr.projectdetail.interactions.Action.OnAppear
import org.koin.compose.koinInject

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinInject(),
    projectId: Long,
    onNavigateToMiniature: (Long) -> Unit,
    onCreteMiniature: () -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId = projectId)) }

    Scaffold(
        topBar = {
            GHTab(projectName = state.project?.name)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (state.projectLoaded && state.project != null) {
                GHText(text = state.project.name, type = TextType.Title)
                state.project.description?.let { description ->
                    GHText(text = description, type = TextType.Description)
                }
                GHProgressBar(percentage = state.project.progress)
                ProjectMiniatures(
                    miniatures = state.project.minis,
                    onNavigateToMiniature = onNavigateToMiniature,
                    onCreteMiniature = onCreteMiniature
                )
            } else {
                LoadingIndicator()
            }
        }
    }

}

@Composable
fun ProjectMiniatures(
    miniatures: List<MiniatureBo>,
    onNavigateToMiniature: (Long) -> Unit,
    onCreteMiniature: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            GHButton(text = "+", onClick = onCreteMiniature)
        }
        items(miniatures) { miniature ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { onNavigateToMiniature(miniature.id) }
            ) {

                VerticalProgress(
                    modifier = Modifier.align(Alignment.CenterStart),
                    progress = miniature.percentage
                )
                GHText(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    text = miniature.name,
                    type = TextType.LabelM
                )
                GHText(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    text = "${miniature.percentage}% completed",
                    type = TextType.LabelM
                )
            }
        }
    }
}