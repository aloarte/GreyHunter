package com.devalr.createproject.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.createproject.R
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.framework.components.add.AddItemDescription
import com.devalr.framework.components.add.AddItemName
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.gh.GHImage
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun AddProjectScreenContent(
    innerPadding: PaddingValues = PaddingValues(),
    projectName: String?,
    projectDescription: String?,
    projectImage: String?,
    editMode: Boolean,
    onNavigateBack: () -> Unit,
    onPickImage: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onAddProject: () -> Unit
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
        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(top = 60.dp)
                .padding(all = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                GHImage(
                    imageUri = projectImage,
                    size = 250.dp,
                    showIcon = true,
                    showMessage = true,
                    onImageClick = onPickImage
                )
            }
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item {
                AddItemName(
                    name = projectName,
                    label = stringResource(R.string.label_project_name),
                    onChangeName = onChangeName
                )
            }
            item { Spacer(modifier = Modifier.height(5.dp)) }
            item {
                AddItemDescription(
                    description = projectDescription,
                    label = stringResource(R.string.label_project_description),
                    onChangeDescription = onChangeDescription
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                GHButton(
                    text = if (editMode) stringResource(R.string.button_edit_project) else stringResource(
                        R.string.button_add_project
                    ),
                    onClick = onAddProject
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddProjectScreenContentPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AddProjectScreenContent(
                projectName = hierotekCircleProject.name,
                projectDescription = hierotekCircleProject.description,
                projectImage = null,
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
                onChangeDescription = {
                    // Do nothing
                },
                onAddProject = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProjectScreenContentPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AddProjectScreenContent(
                projectName = hierotekCircleProject.name,
                projectDescription = hierotekCircleProject.description,
                projectImage = null,
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
                onChangeDescription = {
                    // Do nothing
                },
                onAddProject = {
                    // Do nothing
                }
            )
        }
    }
}