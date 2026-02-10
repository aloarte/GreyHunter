package com.devalr.minidetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.framework.components.ScreenSize.COMPACT
import com.devalr.framework.components.ScreenSize.EXPANDED
import com.devalr.framework.components.ScreenSize.MEDIUM
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.components.rememberScreenSize
import com.devalr.framework.limitSize
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.framework.theme.ProgressYellow
import com.devalr.minidetail.R

@Composable
fun MiniatureInfo(miniature: MiniatureBo, onlyUpdate: Boolean) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MarkedText(
                title = true,
                text = miniature.name
                    .limitSize(
                        when (rememberScreenSize()) {
                            COMPACT -> 20
                            MEDIUM -> 25
                            EXPANDED -> 30
                        }
                    )
                    .capitalize()
            )
            GHText(text = "${(miniature.percentage * 100).toInt()}%", type = TextType.Featured)
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (onlyUpdate) {
            MarkedText(
                text = stringResource(R.string.label_miniature_update),
                color = ProgressYellow,
                doubleBars = true,
                barsSize = 50.dp
            )
        } else {
            GHText(
                modifier = Modifier.height(50.dp),
                text = getMiniatureProgressMessage(progress = miniature.percentage),
                type = TextType.Description,
                italic = true
            )
        }
    }
}

@Composable
private fun getMiniatureProgressMessage(progress: Float): String {
    val res = when (progress) {
        in 0f..0.25f -> R.string.label_miniature_progress_gamification_0
        in 0.25f..0.50f -> R.string.label_miniature_progress_gamification_25
        in 0.50f..0.75f -> R.string.label_miniature_progress_gamification_50
        in 0.75f..0.99f -> R.string.label_miniature_progress_gamification_75
        1.0f -> R.string.label_miniature_progress_gamification_100
        else -> R.string.label_miniature_progress_gamification_error
    }
    return stringResource(res)
}

@Composable
@Preview(showBackground = true)
private fun MiniatureInfoPreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        MiniatureInfo(
            miniature = chronomancer,
            onlyUpdate = false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureInfoPreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        MiniatureInfo(
            miniature = chronomancer,
            onlyUpdate = false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureInfoOnlyUpdatePreviewDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        MiniatureInfo(
            miniature = chronomancer,
            onlyUpdate = true
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MiniatureInfoOnlyUpdatePreviewLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        MiniatureInfo(
            miniature = chronomancer,
            onlyUpdate = true
        )
    }
}