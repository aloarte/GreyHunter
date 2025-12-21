package com.devalr.createproject.components.cards

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
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.deathmark
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.components.cards.getCardWidth
import com.devalr.framework.enum.CardType

@Composable
fun MiniatureCard(
    modifier: Modifier = Modifier,
    miniatureBo: MiniatureBo
) {
    Card(
        modifier = modifier
            .width(getCardWidth(CardType.Miniature))
            .aspectRatio(1.6f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            GHText(text = miniatureBo.name, type = TextType.Title)
            miniatureBo.imageUri?.let {
                GHImage(imageUri = miniatureBo.imageUri, size = 100.dp)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureCardPreviewInHorizontalRow() {
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalArrangement = spacedBy(10.dp)
    ) {
        item {
            MiniatureCard(
                modifier = Modifier.fillMaxSize(),
                miniatureBo = technomancer
            )
        }
        item {
            MiniatureCard(
                modifier = Modifier.fillMaxSize(),
                miniatureBo = deathmark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniatureCardPreviewInVerticalColumn() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = spacedBy(10.dp)
    ) {
        item {
            MiniatureCard(
                modifier = Modifier.fillMaxSize(),
                miniatureBo = technomancer
            )
        }
        item {
            MiniatureCard(
                modifier = Modifier.fillMaxSize(),
                miniatureBo = deathmark
            )
        }
    }
}