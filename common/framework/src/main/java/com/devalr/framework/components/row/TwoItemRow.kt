package com.devalr.framework.components.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> TwoItemRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    when (items.size) {
        1 -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                itemContent(items.first())
            }
        }

        2 -> {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                itemContent(items[0])
                itemContent(items[1])
            }
        }
    }
}