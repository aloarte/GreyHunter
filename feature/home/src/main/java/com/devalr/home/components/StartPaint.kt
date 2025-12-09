package com.devalr.home.components

import androidx.compose.runtime.Composable
import com.devalr.framework.components.GHButton


@Composable
fun StartPaint(onClick: () -> Unit) {
    GHButton(text = "Start painting", onClick = onClick)
}