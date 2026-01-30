package com.devalr.framework.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.devalr.framework.theme.HunterGreen

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
            containerColor = if (invertColors)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.primary,
        ),
        onClick = onClick
    ) {
        val color = if (invertColors) MaterialTheme.colorScheme.surface else Color.White
        if (icon != null) {
            Icon(painter = icon, contentDescription = "", tint = color)
            Spacer(modifier = Modifier.width(15.dp))
        }
        GHText(
            text = text.uppercase(),
            type = TextType.Title,
            textColor = color
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun GHButtonPreviewsDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHButton(text = "Retry", onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(
                text = "Button with icon",
                icon = painterResource(R.drawable.ic_check),
                onClick = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHButtonPreviewsLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            GHButton(text = "Retry", onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(
                text = "Button with icon",
                icon = painterResource(R.drawable.ic_check),
                onClick = {
                    // Do nothing
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GHButtonInvertedPreviewsDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(HunterGreen)
                .padding(10.dp)
        ) {
            GHButton(text = "Retry", invertColors = true, onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", invertColors = true, onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(
                text = "Button with icon",
                invertColors = true,
                icon = painterResource(R.drawable.ic_check),
                onClick = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHButtonInvertedPreviewsLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(HunterGreen)
                .padding(10.dp)
        ) {
            GHButton(text = "Retry", invertColors = true, onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(text = "Retry again with logout", invertColors = true, onClick = {})
            Spacer(modifier = Modifier.height(20.dp))
            GHButton(
                text = "Button with icon",
                invertColors = true,
                icon = painterResource(R.drawable.ic_check),
                onClick = {
                    // Do nothing
                }
            )
        }
    }
}