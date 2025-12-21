package com.devalr.framework.components.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.R
import com.devalr.framework.components.GHRectangularClickableItem

@Composable
fun ImagePickerBottomSheetContent(
    onOpenCamera: () -> Unit,
    onOpenGallery: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GHRectangularClickableItem(text = stringResource(R.string.btn_camera)) {
            onOpenCamera()
        }
        GHRectangularClickableItem(text = stringResource(R.string.btn_gallery)) {
            onOpenGallery()
        }
    }
}

@Preview
@Composable
private fun ImagePickerBottomSheetContentPreview() {
    ImagePickerBottomSheetContent(onOpenGallery = {}, onOpenCamera = {})
}