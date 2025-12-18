package com.devalr.createminiature

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devalr.createminiature.interactions.Action
import com.devalr.createminiature.interactions.Action.OnAddMiniature
import com.devalr.createminiature.interactions.Action.OnAppear
import com.devalr.createminiature.interactions.Action.OnNameChanged
import com.devalr.createminiature.interactions.Event.OnAddedSuccessfully
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.add.AddItemName
import com.devalr.framework.components.dialog.ImagePickerDialog
import org.koin.compose.koinInject

@Composable
fun AddMiniatureScreen(
    viewModel: AddMiniatureViewModel = koinInject(),
    projectId: Long,
    onBackPressed: () -> Unit
) {
    var showImageSourceDialog by remember { mutableStateOf(false) }
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnAddedSuccessfully -> onBackPressed()
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId)) }

    if (showImageSourceDialog) {
        ImagePickerDialog(
            onCloseDialog = { showImageSourceDialog = false },
            onImageChanged = {
                Log.d("ALRALR","onimagechanged")
                viewModel.onAction(Action.OnImageChanged(it))
            })
    }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GHImage(imageUri = state.miniatureImage, size = 100.dp) { showImageSourceDialog = true }
            Spacer(modifier = Modifier.height(10.dp))
            AddItemName(
                name = state.miniatureName,
                label = stringResource(R.string.label_miniature_name)
            ) {
                viewModel.onAction(OnNameChanged(it))
            }
            Spacer(modifier = Modifier.height(10.dp))
            GHButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.button_add_miniature)
            ) {
                viewModel.onAction(OnAddMiniature)
            }
        }
    }
}
