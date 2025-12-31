package com.devalr.projectdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.GHText
import com.devalr.framework.components.GHVerticalShape
import com.devalr.framework.components.TextType
import com.devalr.framework.components.progress.GHProgressBar
import com.devalr.framework.theme.GreyHunterTheme


@Composable
fun ProjectInfo(project: ProjectBo) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GHVerticalShape()
                Spacer(modifier = Modifier.width(10.dp))
                GHText(text = project.name, type = TextType.Featured)

            }
            GHText(text = "${(project.progress * 100).toInt()}%", type = TextType.Featured)
        }
        project.description?.let { description ->
            Spacer(modifier = Modifier.height(20.dp))
            GHText(
                text = description,
                type = TextType.Description,
                italic = true
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        GHProgressBar(percentage = project.progress)
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Preview
@Composable
private fun ProjectInfoLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        ProjectInfo(project = ProjectBo(1, "Project name", "Project description"))
    }
}

@Preview
@Composable
private fun ProjectInfoDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        ProjectInfo(project = ProjectBo(1, "Project name", "Project description"))
    }
}