package com.devalr.framework.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun GHRectangularClickableItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .width(140.dp)
            .aspectRatio(1.8f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Icon(Icons.Default.Search, contentDescription = "Cámara")
            Spacer(modifier = Modifier.width(10.dp))
            GHText(
                text = text,
                type = TextType.Description
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GHCardButtonPreviews() {
    GreyHunterTheme {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = spacedBy(10.dp)
        ) {
            GHRectangularClickableItem(text = "Camera", onClick = {})
            Spacer(modifier = Modifier.width(10.dp))
            GHRectangularClickableItem(text = "Gallery", onClick = {})
        }
    }
}