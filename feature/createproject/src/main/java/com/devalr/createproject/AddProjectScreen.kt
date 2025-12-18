package com.devalr.createproject

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
import com.devalr.createproject.interactions.Action.OnAddProject
import com.devalr.createproject.interactions.Action.OnAppear
import com.devalr.createproject.interactions.Action.OnDescriptionChanged
import com.devalr.createproject.interactions.Action.OnNameChanged
import com.devalr.createproject.interactions.Event.OnAddedSuccessfully
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.add.AddItemDescription
import com.devalr.framework.components.add.AddItemName
import org.koin.compose.koinInject

@Composable
fun AddProjectScreen(viewModel: AddProjectViewModel = koinInject(), onBackPressed: () -> Unit) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnAddedSuccessfully -> onBackPressed()
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear) }

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
                name = state.projectName,
                label = stringResource(R.string.label_project_name)
            ) {
                viewModel.onAction(OnNameChanged(it))
            }
            AddItemDescription(
                description = state.projectDescription,
                label = stringResource(R.string.label_project_description)
            ) {
                viewModel.onAction(OnDescriptionChanged(it))
            }
            GHButton(text = stringResource(R.string.button_add_project)) {
                viewModel.onAction(OnAddProject)
            }
        }
    }
}
