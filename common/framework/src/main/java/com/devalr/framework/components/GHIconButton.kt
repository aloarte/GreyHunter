package com.devalr.framework.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.theme.GreyHunterTheme

@Composable
fun GHIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
    onButtonClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .size(35.dp)
            .shadow(elevation = 6.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = enabled) { onButtonClicked() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = icon,
            contentDescription = "Atrás",
            tint = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GHIconButtonPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(100.dp)
        ) {
            GHIconButton(
                modifier = Modifier.align(Alignment.Center),
                icon = Icons.Default.ArrowBack,
                onButtonClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GHIconButtonPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(100.dp)
        ) {
            GHIconButton(
                modifier = Modifier.align(Alignment.Center),
                icon = Icons.Default.Edit,
                onButtonClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GHIconButtonPreviewDisabledDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(100.dp)
        ) {
            GHIconButton(
                enabled = false,
                modifier = Modifier.align(Alignment.Center),
                icon = Icons.Default.ArrowBack,
                onButtonClicked = {
                    // Do nothing
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GHIconButtonPreviewDisabledLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(100.dp)
        ) {
            GHIconButton(
                enabled = false,
                modifier = Modifier.align(Alignment.Center),
                icon = Icons.Default.Edit,
                onButtonClicked = {
                    // Do nothing
                }
            )
        }
    }
}