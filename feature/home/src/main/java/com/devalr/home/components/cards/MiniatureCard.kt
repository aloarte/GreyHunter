package com.devalr.home.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.deathmark
import com.devalr.domain.model.helpers.immortal
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.components.cards.getCardWidth
import com.devalr.framework.enum.CardType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun MiniatureCard(
    modifier: Modifier = Modifier,
    miniature: MiniatureBo,
    onMiniatureClicked: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .width(getCardWidth(CardType.LastUpdated))
            .aspectRatio(1.6f),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = { onMiniatureClicked(miniature.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GHText(modifier = Modifier.fillMaxWidth(0.70f),text = miniature.name, type = TextType.Title)
                CircularProgressIndicator(
                    progress = { miniature.percentage },
                    modifier = Modifier.size(30.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureCardPreviewInHorizontalRow() {
    GreyHunterTheme(darkTheme = true) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = spacedBy(10.dp)
        ) {
            item {
                MiniatureCard(
                    modifier = Modifier.fillMaxSize(),
                    miniature = immortal,
                    onMiniatureClicked = {}
                )
            }
            item {
                MiniatureCard(
                    modifier = Modifier.fillMaxSize(),
                    miniature = deathmark,
                    onMiniatureClicked = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureCardPreviewInVerticalColumn() {
    GreyHunterTheme {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = spacedBy(10.dp)
        ) {
            item {
                MiniatureCard(
                    modifier = Modifier.fillMaxSize(),
                    miniature = immortal,
                    onMiniatureClicked = {}
                )
            }
            item {
                MiniatureCard(
                    modifier = Modifier.fillMaxSize(),
                    miniature = deathmark,
                    onMiniatureClicked = {}
                )
            }
        }
    }
}