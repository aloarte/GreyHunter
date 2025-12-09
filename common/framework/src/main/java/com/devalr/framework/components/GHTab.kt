package com.devalr.framework.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GHTab(
    projectName: String? = null,
    miniName: String? = null,
    // onProjectClick: () -> Unit = {},
) {
    val backgroundColor = MaterialTheme.colorScheme.primary // Color base del toolbar
    val projectColor = MaterialTheme.colorScheme.onPrimary
    val miniColor = MaterialTheme.colorScheme.secondaryContainer
    val separatorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = projectColor
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                when {
                    //
                    projectName != null && miniName != null -> {
                        Text(
                            text = projectName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = projectColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = ">",
                            style = MaterialTheme.typography.titleMedium,
                            color = separatorColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = miniName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = miniColor
                        )
                    }

                    projectName != null -> {
                        Text(
                            text = projectName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = projectColor
                        )
                    }

                    else -> {
                        Text("GreyHunter")
                    }
                }
            }
        }
    )
}