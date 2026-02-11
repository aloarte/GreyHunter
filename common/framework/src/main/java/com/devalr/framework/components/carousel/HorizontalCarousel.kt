package com.devalr.framework.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

@Composable
fun <T> HorizontalCarousel(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemsPerPage: Int = 1,
    aspectRatio: Float = 1.6f,
    dots: Boolean = false,
    maxItemWidth: Dp = 320.dp,
    neighborDisplayMargin: Dp = 24.dp,
    pageSpacing: Dp = 12.dp,
    content: @Composable (T) -> Unit
) {
    val pagerState: PagerState = rememberPagerState { items.size }
    BoxWithConstraints(modifier = modifier) {
        val availableWidth = this@BoxWithConstraints.maxWidth - neighborDisplayMargin * 2
        val calculatedWidth =
            (availableWidth - pageSpacing * (itemsPerPage - 1)) / itemsPerPage
        val pageWidth = min(calculatedWidth, maxItemWidth)
        val pageHeight = pageWidth / aspectRatio

        Column(modifier = modifier) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = neighborDisplayMargin),
                pageSpacing = pageSpacing,
                pageSize = PageSize.Fixed(pageWidth),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pageHeight)
            ) { page ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                ) {
                    content(items[page])
                }
            }

            if (dots && items.size > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(items.size) { index ->
                        val selected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(if (selected) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected) Color.DarkGray
                                    else Color.LightGray
                                )
                        )
                    }
                }
            }
        }
    }
}