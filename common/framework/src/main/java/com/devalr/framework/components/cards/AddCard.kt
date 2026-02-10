package com.devalr.framework.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.R
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.enum.CardType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun AddCard(modifier: Modifier = Modifier, type: CardType, onCreate: () -> Unit) {
    Card(
        modifier = modifier
            .width(cardWidthFor(type))
            .aspectRatio(1.6f),
        onClick = { onCreate() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GHText(text = stringResource(R.string.btn_create_project), type = TextType.Title)
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
private fun AddCardPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AddCard(type = CardType.Project) {
                // Do nothing
            }
            AddCard(type = CardType.Miniature) {
                // Do nothing
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AddCardPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AddCard(type = CardType.Project) {
                // Do nothing
            }
            AddCard(type = CardType.Miniature) {
                // Do nothing
            }
        }
    }
}