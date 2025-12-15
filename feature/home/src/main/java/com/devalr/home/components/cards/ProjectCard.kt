package com.devalr.home.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.stormlightArchiveProject
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.progress.GHProgressBar
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.components.cards.getCardWidth
import com.devalr.framework.enum.CardType

@Composable
fun ProjectCard(
    modifier: Modifier = Modifier,
    projectBo: ProjectBo,
    onProjectClicked: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .width(getCardWidth(CardType.Project))
            .aspectRatio(1.6f),
        onClick = { onProjectClicked(projectBo.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            GHText(text = projectBo.name, type = TextType.Title)
            projectBo.imageUri?.let {
                GHImage(imageUri = projectBo.imageUri)
            }
            GHText(text = "${projectBo.minis.size} Miniatures", type = TextType.LabelM)
            projectBo.description?.let { description ->
                GHText(text = description, type = TextType.Description, singleLane = true)
            }
            GHProgressBar(percentage = projectBo.percentage)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectCardPreviewInHorizontalRow() {
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalArrangement = spacedBy(10.dp)
    ) {
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = hierotekCircleProject,
                onProjectClicked = {}
            )
        }
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = stormlightArchiveProject,
                onProjectClicked = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectCardPreviewInVerticalColumn() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = spacedBy(10.dp)
    ) {
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = hierotekCircleProject,
                onProjectClicked = {}
            )
        }
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = stormlightArchiveProject,
                onProjectClicked = {}
            )
        }
    }
}