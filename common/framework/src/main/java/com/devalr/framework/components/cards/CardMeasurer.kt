package com.devalr.framework.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devalr.framework.enum.CardType


@Composable
fun getCardWidth(type: CardType): Dp {
    val screenWidthPercentage = when (type) {
        CardType.StartPaintProject -> 0.90f
        CardType.Project -> 0.70f
        CardType.Miniature -> 0.40f
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    return screenWidth * screenWidthPercentage
}