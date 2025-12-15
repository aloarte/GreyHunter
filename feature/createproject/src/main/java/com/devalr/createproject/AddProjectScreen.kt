package com.devalr.createproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.devalr.createproject.interactions.Action.OnAppear
import org.koin.compose.koinInject

@Composable
fun AddProjectScreen(viewModel: AddProjectViewModel = koinInject(), onBackPressed: () -> Unit) {
    viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
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
                .background(Color.Red)
                .padding(innerPadding)
        ) {
            Text("ADD")
        }
    }
}