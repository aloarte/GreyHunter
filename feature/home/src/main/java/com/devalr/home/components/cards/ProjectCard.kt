package com.devalr.home.components.cards

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.ProjectBo
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHProgressBar
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType

@Composable
fun ProjectCard(modifier: Modifier = Modifier, projectBo: ProjectBo) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenWidth * 0.70f
    Card(
        modifier
            .width(cardWidth)
            .aspectRatio(1.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //GHImage(imageUri = projectBo.imageUri)
            GHText(text = projectBo.name, type = TextType.Title)
            GHText(text = "${projectBo.minis.size} Miniatures", type = TextType.LabelL)
            GHText(text = "This is a project of the Necron Hierotek Circle for Killteam games.", type = TextType.Description)
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
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = ProjectBo(name = "Hierotek Circle", percentage = 0.8f)
            )
        }
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = ProjectBo(name = "Hierotek Circle")
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = ProjectBo(name = "Hierotek Circle")
            )
        }
        item {
            ProjectCard(
                modifier = Modifier.fillMaxSize(),
                projectBo = ProjectBo(name = "Hierotek Circle")
            )
        }
    }
}