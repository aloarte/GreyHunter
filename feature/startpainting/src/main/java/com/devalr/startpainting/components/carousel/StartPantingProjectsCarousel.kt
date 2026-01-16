package com.devalr.startpainting.components.carousel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHIconButton
import com.devalr.framework.components.HorizontalCarousel
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.getScreenSize
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.startpainting.components.cards.StartPaintingProjectCard
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import com.devalr.startpainting.model.helpers.hierotekCircleProjectVo
import com.devalr.startpainting.model.helpers.stormlightArchiveProjectVo
import kotlinx.coroutines.launch


@Composable
fun StartPantingProjectsCarousel(
    modifier: Modifier = Modifier,
    projects: List<StartPaintProjectVo>,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {
    val pagerState = rememberPagerState { projects.size }
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        HorizontalCarousel(
            items = projects,
            height = calculateHeight(getScreenSize()),
            pagerState = pagerState,
            neighborDisplayMargin = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) { item ->
            StartPaintingProjectCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(calculateHeight(getScreenSize())),
                projectBo = item,
                onMiniatureSelected = onMiniatureSelected
            )
        }

        if(pagerState.currentPage > 0){
            GHIconButton(
                enabled = pagerState.currentPage > 0,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(8.dp),
                icon = Icons.Default.KeyboardArrowLeft,
                onButtonClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            )
        }
        if(pagerState.currentPage < projects.lastIndex){
            GHIconButton(
                enabled = pagerState.currentPage < projects.lastIndex,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(8.dp),
                icon = Icons.Default.KeyboardArrowRight,
                onButtonClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )
        }
    }
}

@Composable
private fun calculateHeight(screenSize: ScreenSize) = when (screenSize) {
    ScreenSize.SMALL -> 300.dp
    ScreenSize.MEDIUM -> 600.dp
    else -> 600.dp
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        StartPantingProjectsCarousel(
            projects = listOf(
                hierotekCircleProjectVo,
                stormlightArchiveProjectVo,
            ),
            onMiniatureSelected = {
                //Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectsCarouselDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        StartPantingProjectsCarousel(
            projects = listOf(
                hierotekCircleProjectVo,
                stormlightArchiveProjectVo,
            ),
            onMiniatureSelected = {
                //Do nothing
            }
        )
    }
}