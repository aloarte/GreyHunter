package com.devalr.home.components.anim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.R

@Composable
fun EmptyProjects(hasAnyProject: Boolean = false) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_empty))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GHText(
            text = if (hasAnyProject) stringResource(R.string.title_projects_empty_minis_description)
            else stringResource(R.string.title_empty_projects_description),
            type = TextType.Title
        )

        LottieAnimation(
            modifier = Modifier.size(250.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
        GHText(
            text = if (hasAnyProject) stringResource(R.string.label_empty_miniatures_description)
            else stringResource(R.string.label_empty_projects_description),
            type = TextType.Description
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun EmptyProjectsPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            EmptyProjects()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyProjectsPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            EmptyProjects()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsWithEmptyMinisPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            EmptyProjects(hasAnyProject = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsWithEmptyMinisPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            EmptyProjects(hasAnyProject = true)
        }
    }
}