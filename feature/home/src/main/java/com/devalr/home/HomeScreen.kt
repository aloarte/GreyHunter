package com.devalr.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject()
) {
    val state = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

        }
    }

}