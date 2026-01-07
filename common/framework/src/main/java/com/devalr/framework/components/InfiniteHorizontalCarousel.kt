package com.devalr.framework.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun <T> InfiniteHorizontalCarousel(
    items: List<T>,
    content: @Composable (T) -> Unit
) {

    val augmentedMiniatures = remember(items) {
        if (items.size > 1) {
            listOf(items.last()) + items + listOf(items.first())
        } else {
            items
        }
    }
    val lastIndex = augmentedMiniatures.lastIndex
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { augmentedMiniatures.size }
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val current = pagerState.settledPage
            val target = if (current == lastIndex - 1) {
                lastIndex
            } else {
                current + 1
            }
            pagerState.animateScrollToPage(
                page = target,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        when (pagerState.settledPage) {
            0 -> pagerState.scrollToPage(lastIndex - 1)
            lastIndex -> pagerState.scrollToPage(1)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        userScrollEnabled = false
    ) { page ->
        content(augmentedMiniatures[page])
    }

}
