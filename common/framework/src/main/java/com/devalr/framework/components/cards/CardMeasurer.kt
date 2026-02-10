package com.devalr.framework.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.rememberScreenSize
import com.devalr.framework.enum.CardType

@Composable
fun cardWidthFor(type: CardType): Dp {
    val windowSize = rememberScreenSize()

    return when (windowSize) {
        ScreenSize.COMPACT -> {
            when (type) {
                CardType.StartPaintProject -> 320.dp
                CardType.Project -> 300.dp
                CardType.Miniature, CardType.Home -> 240.dp
            }
        }

        ScreenSize.MEDIUM -> {
            when (type) {
                CardType.StartPaintProject -> 360.dp
                CardType.Project -> 340.dp
                CardType.Miniature, CardType.Home -> 260.dp
            }
        }

        ScreenSize.EXPANDED -> {
            when (type) {
                CardType.StartPaintProject -> 380.dp
                CardType.Project -> 360.dp
                CardType.Miniature, CardType.Home -> 280.dp
            }
        }
    }
}

enum class Columns(val count: Int) {
    One(1),
    Two(2),
    Three(3)
}

@Composable
fun columnsForWidth(width: Dp): Columns =
    when {
        width < 600.dp -> Columns.Two
        width < 840.dp -> Columns.Three
        else -> Columns.Three
    }