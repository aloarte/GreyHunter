package com.devalr.framework.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.framework.theme.ProgressGreen
import com.devalr.framework.theme.TextLight

@Composable
fun GHSnackBar(snackBarData: SnackbarData) {
    val customVisuals = snackBarData.visuals as? SnackBarVisualsCustom
    val type = customVisuals?.type ?: SnackBarType.SUCCESS
    val backgroundColor = when (type) {
        SnackBarType.SUCCESS -> ProgressGreen
        SnackBarType.ERROR -> MaterialTheme.colorScheme.error
    }
    val icon = when (type) {
        SnackBarType.SUCCESS -> Icons.Default.CheckCircle
        SnackBarType.ERROR -> Icons.Default.Warning
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
            GHText(
                text = snackBarData.visuals.message,
                type = TextType.LabelMBold,
                textColor = TextLight
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GHSnackBarSuccessPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHSnackBar(
                snackBarData = GHSnackBarData(
                    visuals = SnackBarVisualsCustom(
                        message = "This is a snackbar message",
                        type = SnackBarType.SUCCESS
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GHSnackBarErrorPreviewLightMode() {
    GreyHunterTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHSnackBar(
                snackBarData = GHSnackBarData(
                    visuals = SnackBarVisualsCustom(
                        message = "This is a snackbar message",
                        type = SnackBarType.ERROR
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GHSnackBarSuccessPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHSnackBar(
                snackBarData = GHSnackBarData(
                    visuals = SnackBarVisualsCustom(
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue neque at diam sollicitudin, ac aliquet felis imperdiet",
                        type = SnackBarType.SUCCESS
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GHSnackBarErrorPreviewDarkMode() {
    GreyHunterTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GHSnackBar(
                snackBarData = GHSnackBarData(
                    visuals = SnackBarVisualsCustom(
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue neque at diam sollicitudin, ac aliquet felis imperdiet",
                        type = SnackBarType.ERROR
                    )
                )
            )
        }
    }
}

