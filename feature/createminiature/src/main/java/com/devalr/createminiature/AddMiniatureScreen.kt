package com.devalr.createminiature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devalr.createminiature.interactions.Action
import com.devalr.createminiature.interactions.Action.OnAddMiniature
import com.devalr.createminiature.interactions.Action.OnAppear
import com.devalr.createminiature.interactions.Action.OnNameChanged
import com.devalr.createminiature.interactions.Event.OnAddedSuccessfully
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.add.AddItemImage
import com.devalr.framework.components.add.AddItemName
import org.koin.compose.koinInject

@Composable
fun AddMiniatureScreen(viewModel: AddMiniatureViewModel = koinInject(), projectId:Long, onBackPressed: () -> Unit) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnAddedSuccessfully -> onBackPressed()
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId)) }

    Scaffold(
        topBar = {
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 10.dp)
        ) {
            AddItemName(
                name = state.miniatureName,
                label = stringResource(R.string.label_miniature_name)
            ) {
                viewModel.onAction(OnNameChanged(it))
            }
            AddItemImage()
            GHButton(text = stringResource(R.string.button_add_miniature)) {
                viewModel.onAction(OnAddMiniature)
            }
        }
    }
}
