package com.devalr.framework.components.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> SinglePageCarousel(
    modifier: Modifier = Modifier,
    items: List<T>,
    pagerState: PagerState = rememberPagerState(pageCount = { items.size }),
    neighborDisplayMargin: Dp = 50.dp,
    height: Dp = 400.dp,
    content: @Composable (T) -> Unit
) {
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = neighborDisplayMargin),
            pageSpacing = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) { page ->
            content(items[page])
        }
    }
}