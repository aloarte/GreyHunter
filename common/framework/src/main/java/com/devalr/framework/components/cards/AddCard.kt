package com.devalr.framework.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
import com.devalr.framework.enum.CardType


@Composable
fun AddCard(modifier: Modifier = Modifier, type: CardType, onCreate: () -> Unit) {
    Card(
        modifier = modifier
            .width(getCardWidth(type))
            .aspectRatio(1.6f),
        onClick = { onCreate() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GHText(text = "Add a new project", type = TextType.Title)
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Create new project"
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun AddCardPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        AddCard(type = CardType.Project) {
            // Do nothing
        }
        AddCard(type = CardType.Miniature) {
            // Do nothing
        }
    }

}