package com.devalr.framework.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class TextType {
    Title, Description, LabelXs, LabelS, LabelMBold, LabelM, LabelL, Featured, UltraFeatured
}

@Composable
fun GHText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null,
    singleLane: Boolean = false,
    italic: Boolean = false,
    lineHeight: TextUnit = TextUnit.Unspecified,
    type: TextType
) {
    Text(
        modifier = modifier,
        text = text,
        color = textColor,
        textAlign = textAlign,
        fontWeight = getTextWeight(type),
        lineHeight = lineHeight,
        fontSize = getTextSize(type),
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        maxLines = if (singleLane) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}

private fun getTextSize(type: TextType) = when (type) {
    TextType.UltraFeatured -> 40.sp
    TextType.Featured -> 20.sp
    TextType.Title -> 14.sp
    TextType.Description -> 12.sp
    TextType.LabelXs -> 8.sp
    TextType.LabelS -> 10.sp
    TextType.LabelMBold,
    TextType.LabelM -> 12.sp
    TextType.LabelL -> 14.sp
}

private fun getTextWeight(type: TextType) = when (type) {
    TextType.UltraFeatured -> FontWeight.ExtraBold
    TextType.Featured -> FontWeight.Bold
    TextType.Title -> FontWeight.Bold
    TextType.Description -> FontWeight.Normal
    TextType.LabelXs -> FontWeight.Light
    TextType.LabelS -> FontWeight.Light
    TextType.LabelM -> FontWeight.Light
    TextType.LabelMBold -> FontWeight.Medium
    TextType.LabelL -> FontWeight.Light
}