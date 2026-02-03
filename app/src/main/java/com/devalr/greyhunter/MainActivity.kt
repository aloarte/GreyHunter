package com.devalr.greyhunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devalr.domain.enum.ThemeType
import com.devalr.framework.components.progress.ProvideProgressColors
import com.devalr.framework.components.snackbar.GHSnackBar
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.greyhunter.composables.InitProgressColors
import com.devalr.greyhunter.navigation.NavHost
import com.devalr.greyhunter.tracer.FirebaseTracer
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val darkMode by mainViewModel.darkModeState.collectAsStateWithLifecycle()
            val progressColor by mainViewModel.colorState.collectAsStateWithLifecycle()
            val isDarkTheme = when (darkMode) {
                ThemeType.Light -> false
                ThemeType.Dark -> true
                ThemeType.System -> isSystemInDarkTheme()
            }
            GreyHunterTheme(darkTheme = isDarkTheme) {
                InitProgressColors(progressColor)
                ProvideProgressColors {
                    val systemUiController = rememberSystemUiController()
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = !isDarkTheme
                        )
                    }
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackBarHostState) { data ->
                                GHSnackBar(snackBarData = data)
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        NavHost(snackBarHostState, FirebaseTracer())
                    }
                }
            }
        }
    }
}
