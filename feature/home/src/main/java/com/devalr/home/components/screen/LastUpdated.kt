package com.devalr.home.components.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.ProjectBo
import com.devalr.domain.model.helpers.hierotekCircleProject
import com.devalr.domain.model.helpers.immortal
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.enum.CardType
import com.devalr.home.components.cards.MiniatureCard
import com.devalr.home.components.cards.ProjectCard

@Composable
fun LastUpdated(
    project: ProjectBo?,
    miniature: MiniatureBo?,
    onProjectClicked: (Long) -> Unit,
    onMiniatureClicked: (Long) -> Unit
) {
    if (project != null && miniature != null) {
        Column(modifier = Modifier.padding(20.dp)) {
            GHText(text = "Updated recently", type = TextType.Title)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProjectCard(
                    project = project,
                    cardType = CardType.LastUpdated,
                    onProjectClicked = onProjectClicked
                )
                MiniatureCard(
                    miniature = miniature,
                    onMiniatureClicked = onMiniatureClicked
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LastUpdatedPreview() {
    LastUpdated(
        project = hierotekCircleProject,
        miniature = immortal,
        onProjectClicked = {
            // Do nothing
        },
        onMiniatureClicked = {
            // Do nothing
        }
    )
}
