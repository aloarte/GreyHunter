package com.devalr.createminiature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.createminiature.R
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.add.AddItemName
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.gh.GHImage
import com.devalr.framework.components.markedtext.TopTitleText
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun AddMiniatureScreenContent(
    innerPadding: PaddingValues = PaddingValues(),
    projectName: String?,
    miniatureName: String?,
    miniatureImage: String?,
    editMode: Boolean,
    onNavigateBack: () -> Unit,
    onPickImage: () -> Unit,
    onChangeName: (String) -> Unit,
    onAddMiniature: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            onNavigateBack = onNavigateBack
        )
        projectName?.let {
            TopTitleText(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .align(Alignment.TopStart),
                text = it
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(top = 60.dp)
                .padding(all = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GHImage(
                imageUri = miniatureImage,
                size = 250.dp,
                showMessage = true,
                onImageClick = onPickImage
            )
            Spacer(modifier = Modifier.height(30.dp))
            AddItemName(
                name = miniatureName,
                label = stringResource(R.string.label_miniature_name),
                onChangeName = onChangeName
            )
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(
                text = if (editMode) stringResource(R.string.button_edit_miniature) else stringResource(
                    R.string.button_add_miniature
                ),
                onClick = onAddMiniature
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMiniatureScreenContentPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AddMiniatureScreenContent(
                projectName = hierotekCircleProject.name,
                miniatureName = technomancer.name,
                miniatureImage = null,
                editMode = false,
                onNavigateBack = {
                    // Do nothing
                },
                onPickImage = {
                    // Do nothing
                },
                onChangeName = {
                    // Do nothing
                },
                onAddMiniature = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMiniatureScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AddMiniatureScreenContent(
                projectName = hierotekCircleProject.name + " Super special name",
                miniatureName = technomancer.name,
                miniatureImage = null,
                editMode = false,
                onNavigateBack = {
                    // Do nothing
                },
                onPickImage = {
                    // Do nothing
                },
                onChangeName = {
                    // Do nothing
                },
                onAddMiniature = {
                    // Do nothing
                }
            )
        }
    }
}