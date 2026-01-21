package com.devalr.greyhunter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devalr.domain.enum.AppearanceType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.greyhunter.navigation.NavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode by mainViewModel.darkModeState.collectAsStateWithLifecycle()
            val isDarkTheme = when (darkMode) {
                AppearanceType.Light -> false
                AppearanceType.Dark -> true
                AppearanceType.System -> isSystemInDarkTheme()
            }
            Log.d("ALRALR","Is dark theme: $isDarkTheme. Mode: $darkMode")
            GreyHunterTheme(darkTheme = isDarkTheme) {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavHost()
                }
            }
        }
    }
}
