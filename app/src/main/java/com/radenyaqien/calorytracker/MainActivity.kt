package com.radenyaqien.calorytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.radenyaqien.calorytracker.navigation.navigate
import com.radenyaqien.calorytracker.ui.theme.CaloryTrackerTheme
import com.radenyaqien.core.navigation.Route
import com.radenyaqien.onboarding_presentation.activity.ActivityScreen
import com.radenyaqien.onboarding_presentation.age.AgeScreen
import com.radenyaqien.onboarding_presentation.gender.GenderScreen
import com.radenyaqien.onboarding_presentation.goal.GoalScreen
import com.radenyaqien.onboarding_presentation.height.HeightScreen
import com.radenyaqien.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.radenyaqien.onboarding_presentation.weight.WeightScreen
import com.radenyaqien.onboarding_presentation.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloryTrackerTheme(darkTheme = false) {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                MainNavigation(navController, scaffoldState)
            }
        }
    }

    @Composable
    private fun MainNavigation(
        navController: NavHostController,
        scaffoldState: ScaffoldState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Route.WELCOME
            ) {

                composable(Route.WELCOME) {
                    WelcomeScreen(onNavigate = navController::navigate)
                }
                composable(Route.GENDER) {
                    GenderScreen(onNavigate = navController::navigate)
                }
                composable(Route.AGE) {
                    AgeScreen(
                        scaffoldState = scaffoldState,
                        onNavigate = navController::navigate,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                composable(Route.HEIGHT) {
                    HeightScreen(
                        scaffoldState = scaffoldState,
                        onNavigate = navController::navigate,
                        modifier = Modifier.padding(paddingValues),
                    )
                }
                composable(Route.WEIGHT) {
                    WeightScreen(
                        scaffoldState = scaffoldState,
                        onNavigate = navController::navigate,
                    )
                }
                composable(Route.ACTIVITY) {
                    ActivityScreen(
                        onNavigate = navController::navigate
                    )
                }
                composable(Route.GOAL) {
                    GoalScreen(
                        onNavigate = navController::navigate
                    )
                }

                composable(Route.NUTRIENT_GOAL) {
                    NutrientGoalScreen(
                        scaffoldState = scaffoldState,
                        onNavigate = navController::navigate
                    )
                }
                composable(Route.TRACKER_OVERVIEW) {

                }
            }
        }

    }


}