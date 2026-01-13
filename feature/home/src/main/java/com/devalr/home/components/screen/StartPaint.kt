package com.devalr.home.components.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType


@Composable
fun StartPaint(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GHText(
            text = "Pick one or more miniatures and start painting to upgrade your project progress!",
            type = TextType.Description
        )
        Spacer(modifier = Modifier.height(10.dp))
        GHButton(text = "Start painting", onClick = onClick)
    }
}