package com.devalr.framework.components.markedtext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun TopTitleText(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .height(35.dp)
            .shadow(elevation = 6.dp, shape = CircleShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            GHText(text = text.limitSize(30), type = TextType.LabelMBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopTitleTextPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopTitleText(text = " Super special name")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopTitleTextPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopTitleText(text = " Super special name")
        }
    }
}

fun String.limitSize(limit: Int): String {
    return if (this.length > limit) {
        "${this.take(limit)}..."
    } else {
        this
    }
}