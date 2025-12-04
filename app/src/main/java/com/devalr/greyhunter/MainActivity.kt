package com.devalr.greyhunter

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.devalr.framework.theme.GreyHunterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContent {
            GreyHunterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    /*val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (sharedPref.getBoolean("CITY_SELECTED", false)) {
                            NavScreen.DayWeather.route
                        } else {
                            NavScreen.CitySelection.route
                        }
                    ) {
                        composable(NavScreen.DayWeather.route) {
                            WeatherScreen {
                                navController.navigate(NavScreen.CitySelection.route)
                            }
                        }
                        composable(NavScreen.CitySelection.route) {
                            CitySelectorScreen {
                                sharedPref.edit().putBoolean("CITY_SELECTED", true).apply()
                                navController.navigate(NavScreen.DayWeather.route)
                            }
                        }
                    }*/
                }
            }
        }*/
    }
}
