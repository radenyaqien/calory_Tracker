package com.radenyaqien.tracker_presentation.tracker_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radenyaqien.core.data.preferences.Preferences
import com.radenyaqien.tracker_domain.use_case.TrackerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    preferences: Preferences,
    private val trackerUseCase: TrackerUseCase
) : ViewModel() {

    var state by mutableStateOf(TrackerOverviewState())
        private set
    private var getFoodForDateJob: Job? = null


    init {
        refreshFoods()
        preferences.saveShouldShowOnBoarding(false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {


            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    trackerUseCase.deleteTrackedFood(event.trackedFood)
                    refreshFoods()
                }
            }

            TrackerOverviewEvent.OnNextdayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                refreshFoods()
            }

            TrackerOverviewEvent.OnPreviousdayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                refreshFoods()
            }

            is TrackerOverviewEvent.OnToggleMealClick -> {
                state = state.copy(
                    meals = state.meals.map {
                        if (it.name == event.meal.name) {
                            it.copy(
                                isExpanded = (!it.isExpanded)
                            )
                        } else it
                    }
                )
            }
        }
    }

    private fun refreshFoods() {
        getFoodForDateJob?.cancel()
        getFoodForDateJob = trackerUseCase.getFoodsForDate(state.date)
            .onEach { foods ->
                val nutrientResult = trackerUseCase.calculateMealNutrients(foods)
                state = state.copy(
                    totalCarbs = nutrientResult.totalCarbs,
                    totalProtein = nutrientResult.totalProtein,
                    totalFat = nutrientResult.totalFat,
                    totalCalories = nutrientResult.totalcalories,
                    carbsGoal = nutrientResult.carboGoal,
                    proteinGoal = nutrientResult.proteinGoal,
                    fatGoal = nutrientResult.fatGoal,
                    caloriesGoal = nutrientResult.caloriesGoal,
                    trackedFoods = foods,
                    meals = state.meals.map {
                        val nutrientForMeal = nutrientResult.mealNutrients[it.mealType]
                            ?: return@map it.copy(
                                carbs = 0,
                                protein = 0,
                                fat = 0,
                                calories = 0
                            )
                        it.copy(
                            carbs = nutrientForMeal.carbs,
                            protein = nutrientForMeal.protein,
                            fat = nutrientForMeal.fat,
                            calories = nutrientForMeal.calories
                        )
                    }
                )
            }.launchIn(
                viewModelScope
            )

    }

}