package com.radenyaqien.calorytracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth.assertThat
import com.radenyaqien.calorytracker.repository.TrackerRepositoryFake
import com.radenyaqien.calorytracker.ui.theme.CaloryTrackerTheme
import com.radenyaqien.core.data.preferences.Preferences
import com.radenyaqien.core.domain.model.ActivityLevel
import com.radenyaqien.core.domain.model.Gender
import com.radenyaqien.core.domain.model.GoalType
import com.radenyaqien.core.domain.model.UserInfo
import com.radenyaqien.core.domain.use_case.FilterOutDigits
import com.radenyaqien.core.navigation.Route
import com.radenyaqien.tracker_domain.model.TrackableFood
import com.radenyaqien.tracker_domain.use_case.CalculateMealNutrients
import com.radenyaqien.tracker_domain.use_case.DeleteTrackedFood
import com.radenyaqien.tracker_domain.use_case.GetFoodsForDate
import com.radenyaqien.tracker_domain.use_case.SearchFood
import com.radenyaqien.tracker_domain.use_case.TrackFood
import com.radenyaqien.tracker_domain.use_case.TrackerUseCase
import com.radenyaqien.tracker_presentation.search.SearchScreen
import com.radenyaqien.tracker_presentation.search.SearchViewModel
import com.radenyaqien.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.radenyaqien.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCase: TrackerUseCase
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setup() {

        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            level = ActivityLevel.Medium,
            type = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        repositoryFake = TrackerRepositoryFake()
        trackerUseCase = TrackerUseCase(
            trackFood = TrackFood(repositoryFake),
            searchFood = SearchFood(repositoryFake),
            getFoodsForDate = GetFoodsForDate(repositoryFake),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCase = trackerUseCase
        )
        searchViewModel = SearchViewModel(
            trackerUseCase = trackerUseCase,
            filterOutDigits = FilterOutDigits()
        )

        composeRule.activity.setContent {
            CaloryTrackerTheme {
                val scaffoldState = rememberScaffoldState()
                navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW,
                        modifier = Modifier.padding(paddingValues = paddingValues)
                    ) {

                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { meal, day, month, year ->
                                    navController.navigate("${Route.SEARCH}/$meal/$day/$month/$year")
                                }, viewModel = trackerOverviewViewModel
                            )
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
                                }, viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repositoryFake.results = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinPer100g = 5,
                carbsPer100g = 50,
                fatPer100g = 1
            )
        )
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()
        composeRule.onNodeWithText("Add Breakfast")
            .assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Breakfast").performClick()
        composeRule.onNodeWithText("Add Breakfast").assertIsDisplayed()
        composeRule.onNodeWithText("Add Breakfast")
            .performClick()

        assertThat(
            navController.currentDestination?.route?.startsWith(Route.SEARCH)
        ).isTrue()
        composeRule
            .onNodeWithTag("search_textfield")
            .performTextInput("banana")
        composeRule.onNodeWithContentDescription("Search...")
            .performClick()
        composeRule
            .onNodeWithText("Carbs")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toStr())
        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.TRACKER_OVERVIEW)
        )
        composeRule
            .onAllNodesWithText(expectedCarbs.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedProtein.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedFat.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedCalories.toStr())
            .onFirst()
            .assertIsDisplayed()
    }
}