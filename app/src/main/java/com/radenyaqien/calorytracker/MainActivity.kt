package com.radenyaqien.calorytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.radenyaqien.calorytracker.navigation.navigate
import com.radenyaqien.calorytracker.ui.theme.CaloryTrackerTheme
import com.radenyaqien.core.navigation.Route
import com.radenyaqien.onboarding_presentation.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloryTrackerTheme {
                val navController = rememberNavController()
                MainNavigation(navController)

            }
        }
    }

    @Composable
    private fun MainNavigation(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Route.WELCOME
        ) {

            composable(Route.WELCOME) {
                WelcomeScreen(navController::navigate)
            }
            composable(Route.AGE) {

            }
        }
    }


}