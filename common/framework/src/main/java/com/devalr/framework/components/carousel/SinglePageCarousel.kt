package com.devalr.framework.components.carousel

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
    content: @Composable (T) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = neighborDisplayMargin),
        pageSpacing = 1.dp,
        modifier = modifier.fillMaxSize()
    ) { page ->
        content(items[page])
    }

}