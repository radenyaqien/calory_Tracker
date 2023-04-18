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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.radenyaqien.calorytracker.ui.theme.CaloryTrackerTheme
import com.radenyaqien.core.data.preferences.Preferences
import com.radenyaqien.core.navigation.Route
import com.radenyaqien.onboarding_presentation.activity.ActivityScreen
import com.radenyaqien.onboarding_presentation.age.AgeScreen
import com.radenyaqien.onboarding_presentation.gender.GenderScreen
import com.radenyaqien.onboarding_presentation.goal.GoalScreen
import com.radenyaqien.onboarding_presentation.height.HeightScreen
import com.radenyaqien.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.radenyaqien.onboarding_presentation.weight.WeightScreen
import com.radenyaqien.onboarding_presentation.welcome.WelcomeScreen
import com.radenyaqien.tracker_presentation.search.SearchScreen
import com.radenyaqien.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences
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
        val shouldShowOnboarding = preferences.loadShouldShowOnBoarding()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = if (shouldShowOnboarding) Route.WELCOME else Route.TRACKER_OVERVIEW
            ) {

                composable(Route.WELCOME) {
                    WelcomeScreen(onNextClick = {
                        navController.navigate(Route.GENDER)
                    })
                }
                composable(Route.GENDER) {
                    GenderScreen(onNextClick = {
                        navController.navigate(Route.AGE)
                    })
                }
                composable(Route.AGE) {
                    AgeScreen(
                        scaffoldState = scaffoldState,
                        onNextClick = {
                            navController.navigate(Route.HEIGHT)
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                composable(Route.HEIGHT) {
                    HeightScreen(
                        scaffoldState = scaffoldState,
                        onNextClick = {
                            navController.navigate(Route.WEIGHT)
                        },
                        modifier = Modifier.padding(paddingValues),
                    )
                }
                composable(Route.WEIGHT) {
                    WeightScreen(
                        scaffoldState = scaffoldState,
                        onNextClick = {
                            navController.navigate(Route.ACTIVITY)
                        },
                    )
                }
                composable(Route.ACTIVITY) {
                    ActivityScreen(
                        onNextClick = {
                            navController.navigate(Route.GOAL)
                        }
                    )
                }
                composable(Route.GOAL) {
                    GoalScreen(
                        onNextClick = {
                            navController.navigate(Route.NUTRIENT_GOAL)
                        }
                    )
                }

                composable(Route.NUTRIENT_GOAL) {
                    NutrientGoalScreen(
                        scaffoldState = scaffoldState,
                        onNextClick = {
                            navController.navigate(Route.TRACKER_OVERVIEW)
                        }
                    )
                }
                composable(Route.TRACKER_OVERVIEW) {
                    TrackerOverviewScreen(onNavigateToSearch = { meal, day, month, year ->
                        navController.navigate("${Route.SEARCH}/$meal/$day/$month/$year")
                    })
                }
                composable(
                    route = Route.SEARCH + "/{mealName}/{day}/{month}/{year}",
                    arguments = listOf(
                        navArgument("mealName") {
                            type = NavType.StringType
                        },
                        navArgument("day") {
                            type = NavType.IntType
                        },
                        navArgument("month") {
                            type = NavType.IntType
                        },
                        navArgument("year") {
                            type = NavType.IntType
                        },
                    )
                ) {
                    val mealName = requireNotNull(it.arguments?.getString("mealName"))
                    val day = requireNotNull(it.arguments?.getInt("day"))
                    val month = requireNotNull(it.arguments?.getInt("month"))
                    val year = requireNotNull(it.arguments?.getInt("year"))

                    SearchScreen(
                        scaffoldState = scaffoldState,
                        mealName = mealName,
                        dayOfMonth = day,
                        month = month,
                        year = year,
                        onNavigateUp = {
                            navController.navigateUp()
                        })
                }
            }
        }

    }


}