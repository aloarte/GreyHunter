package com.devalr.framework.components.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devalr.framework.R
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType


@Composable
fun PermissionBottomSheetContent(
    isPermanentlyDenied: Boolean,
    onAccept: () -> Unit,
    onGoToSettings: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GHText(
            text = if (isPermanentlyDenied) stringResource(R.string.bs_permission_denied) else stringResource(
                R.string.bs_permission_required_camera
            ),
            type = TextType.Title
        )
        Spacer(modifier = Modifier.height(10.dp))
        GHText(
            text = if (isPermanentlyDenied) stringResource(R.string.bs_permission_denied_description)
            else stringResource(R.string.bs_permission_required_description),
            type = TextType.LabelL
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(stringResource(R.string.btn_cancel))
            }
            Button(
                onClick = if (isPermanentlyDenied) onGoToSettings else onAccept,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                GHText(
                    text = if (isPermanentlyDenied) stringResource(R.string.btn_go_settings)
                    else stringResource(R.string.btn_accept),
                    type = TextType.LabelL
                )
            }
        }
    }
}
