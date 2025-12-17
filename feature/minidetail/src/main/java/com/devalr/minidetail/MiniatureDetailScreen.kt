package com.devalr.minidetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.devalr.domain.enum.MilestoneType
import com.devalr.domain.enum.MilestoneType.Assembled
import com.devalr.domain.enum.MilestoneType.Base
import com.devalr.domain.enum.MilestoneType.BaseColored
import com.devalr.domain.enum.MilestoneType.Details
import com.devalr.domain.enum.MilestoneType.Primed
import com.devalr.framework.components.GHTab
import com.devalr.framework.components.GHText
import com.devalr.framework.components.LoadingIndicator
import com.devalr.framework.components.TextType
import com.devalr.minidetail.interactions.Action.OnAppear
import com.devalr.minidetail.interactions.Action.OnMilestone
import org.koin.compose.koinInject

@Composable
fun MiniatureDetailScreen(
    viewModel: MiniatureDetailViewModel = koinInject(),
    miniatureId: Long
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(miniatureId)) }

    Scaffold(
        topBar = {
            GHTab(projectName = state.parentProject?.name, miniName = state.miniature?.name)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Opened $miniatureId")

            if (state.miniatureLoaded && state.miniature != null) {
                MiniatureMilestone(
                    type = Assembled,
                    milestoneAchieved = state.miniature.completion.isAssembled
                ) { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }
                MiniatureMilestone(
                    type = Primed,
                    milestoneAchieved = state.miniature.completion.isPrimed
                ) { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }
                MiniatureMilestone(
                    type = BaseColored,
                    milestoneAchieved = state.miniature.completion.isBaseColored
                ) { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }
                MiniatureMilestone(
                    type = Details,
                    milestoneAchieved = state.miniature.completion.isDetailed
                ) { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }
                MiniatureMilestone(
                    type = Base,
                    milestoneAchieved = state.miniature.completion.baseIsFinished
                ) { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }

            } else {
                LoadingIndicator()
            }

        }
    }

}

@Composable
fun MiniatureMilestone(
    type: MilestoneType,
    milestoneAchieved: Boolean,
    updateMilestone: (MilestoneType, Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        GHText(text = getMiniatureMilestoneText(type), type = TextType.LabelM)
        Checkbox(
            checked = milestoneAchieved,
            onCheckedChange = { updateMilestone(type, it) }
        )
    }
}

@Composable
fun getMiniatureMilestoneText(type: MilestoneType) = when (type) {
    Assembled -> "Assembled"
    Primed -> "Primed"
    BaseColored -> "Base Colored"
    Details -> "Details"
    Base -> "Base Finished"
}