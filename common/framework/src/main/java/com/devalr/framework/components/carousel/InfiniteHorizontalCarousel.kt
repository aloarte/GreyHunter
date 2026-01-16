package com.devalr.framework.components.carousel

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.framework.theme.LightGray
import kotlinx.coroutines.delay

@Composable
fun <T> InfiniteHorizontalCarousel(
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    transitionDelayMillis: Long = 5000,
    animationSpec: AnimationSpec<Float> = spring(),
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

    // Infinite looping through the carousel
    LaunchedEffect(Unit) {
        while (true) {
            delay(transitionDelayMillis)
            val current = pagerState.settledPage
            val target = if (current == lastIndex - 1) {
                lastIndex
            } else {
                current + 1
            }
            pagerState.animateScrollToPage(
                page = target,
                animationSpec = animationSpec
            )
        }
    }

    // Triggers the magical effect to made the carousel infinite
    LaunchedEffect(pagerState.settledPage) {
        when (pagerState.settledPage) {
            0 -> pagerState.scrollToPage(lastIndex - 1)
            lastIndex -> pagerState.scrollToPage(1)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        userScrollEnabled = userScrollEnabled
    ) { page ->
        content(augmentedMiniatures[page])
    }
}


@Preview(showBackground = true)
@Composable
private fun InfiniteHorizontalCarousel1ItemPreview() {
    GreyHunterTheme(darkTheme = true) {
        InfiniteHorizontalCarousel(
            modifier = Modifier.padding(vertical = 10.dp),
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            items = listOf("String 1"),
            userScrollEnabled = false
        ) { item ->
            Text(text = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfiniteHorizontalCarouselManyItemsPreview() {
    GreyHunterTheme(darkTheme = true) {
        InfiniteHorizontalCarousel(
            modifier = Modifier.padding(vertical = 10.dp),
            items = listOf("String 1", "String 2", "String 3", "String 4", "String 5"),
            userScrollEnabled = false
        ) { item ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfiniteHorizontalCarouselManyItemsModifiedAnimationAndDelayPreview() {
    GreyHunterTheme(darkTheme = true) {
        InfiniteHorizontalCarousel(
            modifier = Modifier.padding(vertical = 10.dp),
            transitionDelayMillis = 2000,
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            items = listOf("String 1", "String 2", "String 3", "String 4", "String 5"),
            userScrollEnabled = false
        ) { item ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item)
            }
        }
    }
}

