package com.devalr.createminiature

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devalr.createminiature.interactions.Action.OnAddMiniature
import com.devalr.createminiature.interactions.Action.OnAppear
import com.devalr.createminiature.interactions.Action.OnImageChanged
import com.devalr.createminiature.interactions.Action.OnNameChanged
import com.devalr.createminiature.interactions.Event.OnAddedSuccessfully
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.add.AddItemName
import org.koin.compose.koinInject

@Composable
fun AddMiniatureScreen(
    viewModel: AddMiniatureViewModel = koinInject(),
    projectId: Long,
    onBackPressed: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.onAction(OnImageChanged(it))
            }
        }
    )
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnAddedSuccessfully -> onBackPressed()
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId)) }

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
            GHImage(imageUri = state.miniatureImage, size = 100.dp) {
                galleryLauncher.launch("image/*")
            }
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
