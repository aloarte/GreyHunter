package com.devalr.home.components.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType


@Composable
fun GamificationMessage() {
    Column(modifier = Modifier.padding(20.dp)) {
        GHText(text = "Go on, you almost finished all your projects!", type = TextType.Description, italic = true)
    }

}
