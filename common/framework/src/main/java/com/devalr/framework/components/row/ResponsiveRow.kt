@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.devalr.framework.components.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.cards.columnsForWidth

@Composable
fun <T> ResponsiveRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1.6f,
    spacing: Dp = 16.dp,
    itemContent: @Composable (T) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val columns = columnsForWidth(maxWidth).count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            repeat(columns) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(aspectRatio)
                ) {
                    items.getOrNull(index)?.let {
                        itemContent(it)
                    }
                }
            }
        }
    }
}