package com.devalr.framework.components.markedtext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHText
import com.devalr.framework.components.GHVerticalShape
import com.devalr.framework.components.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.framework.theme.ProgressYellow

@Composable
fun MarkedText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    title: Boolean = false,
    fillSpace: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
    doubleBars: Boolean = false,
    barsSize: Dp = 25.dp
) {
    Row(
        modifier = modifier.then(if (fillSpace) Modifier.fillMaxWidth() else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (doubleBars) Arrangement.SpaceBetween else Arrangement.Absolute.Left
    ) {
        GHVerticalShape(color = color, height = barsSize)
        if (!(fillSpace && doubleBars)) Spacer(modifier = Modifier.width(10.dp))
        GHText(
            modifier = Modifier.then(
                if (doubleBars && fillSpace) {
                    Modifier.fillMaxWidth(0.9f)
                } else Modifier
            ),
            textAlign = TextAlign.Start,
            textColor = textColor,
            text = text,
            type = if (title) TextType.Featured else TextType.Description
        )
        if (doubleBars) {
            if (!fillSpace) Spacer(modifier = Modifier.width(10.dp))
            GHVerticalShape(color = color, height = barsSize)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MarkedTextPreviewDefault() {
    GreyHunterTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            MarkedText(text = "Text")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MarkedTextPreviewTitle() {
    GreyHunterTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            MarkedText(fillSpace = true, title = true, text = "Text")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MarkedTextPreview2Bars() {
    GreyHunterTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            MarkedText(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue neque at diam sollicitudin, ac aliquet felis imperdiet",
                barsSize = 50.dp,
                doubleBars = true,
                color = ProgressYellow
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MarkedTextPreview2BarsFilSpace() {
    GreyHunterTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            MarkedText(
                fillSpace = true,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue neque at diam sollicitudin, ac aliquet felis imperdiet",
                barsSize = 50.dp,
                doubleBars = true,
                color = ProgressYellow
            )
        }
    }
}