package com.devalr.home.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.progress.GHProgressBar
import com.devalr.framework.components.progress.LocalProgressColors
import com.devalr.framework.enum.CardType
import com.devalr.framework.limitSize
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun ProjectCard(
    modifier: Modifier = Modifier,
    project: ProjectBo,
    cardType: CardType = CardType.Project,
    onOpenProject: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.6f),

        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        onClick = { onOpenProject(project.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            GHText(
                text = project.name
                    .capitalize()
                    .let { if (cardType != CardType.Project) it.limitSize(22) else it },
                type = TextType.Title
            )
            if (cardType == CardType.Project) GHText(
                text = "${project.minis.size} Miniatures",
                type = TextType.LabelM
            )
            GHProgressBar(
                percentage = project.progress,
                spectrum = LocalProgressColors.current,
                height = if (cardType == CardType.Project) 8.dp else 5.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectCardPreviewInHorizontalRow() {
    GreyHunterTheme(darkTheme = true) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = spacedBy(10.dp)
        ) {
            item {
                ProjectCard(
                    modifier = Modifier.fillMaxSize(),
                    project = hierotekCircleProject,
                    onOpenProject = {}
                )
            }
            item {
                ProjectCard(
                    modifier = Modifier.fillMaxSize(),
                    project = stormlightArchiveProject,
                    onOpenProject = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectCardPreviewInVerticalColumn() {
    GreyHunterTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            verticalArrangement = spacedBy(10.dp)
        ) {
            item {
                ProjectCard(
                    modifier = Modifier.fillMaxSize(),
                    project = hierotekCircleProject,
                    onOpenProject = {}
                )
            }
            item {
                ProjectCard(
                    modifier = Modifier.fillMaxSize(),
                    project = stormlightArchiveProject,
                    onOpenProject = {}
                )
            }
        }
    }
}