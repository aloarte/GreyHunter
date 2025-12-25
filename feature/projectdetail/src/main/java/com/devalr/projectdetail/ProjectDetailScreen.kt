package com.devalr.projectdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.LoadingIndicator
import com.devalr.framework.components.TextType
import com.devalr.framework.components.progress.VerticalProgress
import com.devalr.projectdetail.components.ProjectDetailScreenContent
import com.devalr.projectdetail.interactions.Action.OnAppear
import org.koin.compose.koinInject

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinInject(),
    projectId: Long,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId = projectId)) }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        if (state.projectLoaded && state.project != null) {
            ProjectDetailScreenContent(
                innerPadding = innerPadding,
                project = state.project,
                onNavigateToMiniature = onNavigateToMiniature,
                onCreateMiniature = onCreateMiniature,
                onBackPressed = {},
                onEditPressed = {}
            )
        } else {
            LoadingIndicator()
        }
    }

}
