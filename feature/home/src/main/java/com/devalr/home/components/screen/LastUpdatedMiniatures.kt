package com.devalr.home.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.immortal
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.home.R
import com.devalr.home.components.cards.MiniatureCard

@Composable
fun LastUpdatedMiniatures(
    miniatures: List<MiniatureBo>,
    onMiniatureClicked: (Long) -> Unit
) {
    if (miniatures.isNotEmpty()) {
        Column(modifier = Modifier.padding(25.dp)) {
            MarkedText(text = stringResource(R.string.title_last_updated_miniatures), title = true)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                miniatures.forEach {
                    MiniatureCard(
                        miniature = it,
                        onMiniatureClicked = onMiniatureClicked
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LastUpdatedMiniaturesPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LastUpdatedMiniatures(
                miniatures = listOf(immortal, chronomancer),
                onMiniatureClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LastUpdatedMiniaturesPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LastUpdatedMiniatures(
                miniatures = listOf(immortal, chronomancer),
                onMiniatureClicked = {
                    // Do nothing
                }
            )
        }
    }
}