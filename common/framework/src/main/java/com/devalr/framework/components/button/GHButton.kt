package com.devalr.framework.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.R
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.framework.theme.LightGray

@Composable
fun GHButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    invertColors: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(50.dp),
        enabled = enabled,
        shape = RoundedCornerShape(30),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (invertColors) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (invertColors) MaterialTheme.colorScheme.surfaceVariant else /*MaterialTheme.colorScheme.onPrimary*/ Color.White
        ),
        onClick = onClick
    ) {
        if (icon != null) {
            Icon(painter = icon, contentDescription = "")
            Spacer(modifier = Modifier.width(15.dp))
        }
        GHText(
            text = text.uppercase(),
            type = TextType.Title,
            textColor = if (invertColors) Color.Black else Color.White
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun GHButtonPreviewsDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHButton(text = "Retry", onClick = {})
            VerticalDivider(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", onClick = {})
            VerticalDivider(modifier = Modifier.height(20.dp))
            GHButton(
                text = "Button with icon",
                icon = painterResource(R.drawable.ic_check),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHButtonInvertedPreviewsDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.background(LightGray)) {
                GHButton(text = "Retry", invertColors = true, onClick = {})
                VerticalDivider(modifier = Modifier.height(20.dp))
                GHButton(text = "Retry again with logout", invertColors = true, onClick = {})
                VerticalDivider(modifier = Modifier.height(20.dp))
                GHButton(
                    text = "Button with icon",
                    invertColors = true,
                    icon = painterResource(R.drawable.ic_check),
                    onClick = {}
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GHButtonPreviewsLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHButton(text = "Retry", onClick = {})
            VerticalDivider(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", onClick = {})
            VerticalDivider(modifier = Modifier.height(20.dp))
            GHButton(
                text = "Button with icon",
                icon = painterResource(R.drawable.ic_check),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHButtonInvertedPreviewsLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.background(LightGray)) {
                GHButton(text = "Retry", invertColors = true, onClick = {})
                VerticalDivider(modifier = Modifier.height(20.dp))
                GHButton(text = "Retry again with logout", invertColors = true, onClick = {})
                VerticalDivider(modifier = Modifier.height(20.dp))
                GHButton(
                    text = "Button with icon",
                    invertColors = true,
                    icon = painterResource(R.drawable.ic_check),
                    onClick = {}
                )
            }
        }
    }
}