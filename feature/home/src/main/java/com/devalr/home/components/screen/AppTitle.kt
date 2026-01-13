package com.devalr.home.components.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType

@Composable
fun AppTitle() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        GHText(text = "Grey Hunter", type = TextType.Featured)
    }
}