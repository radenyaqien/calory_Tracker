package com.radenyaqien.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radenyaqien.core.domain.preferences.Preferences
import com.radenyaqien.core.domain.use_case.FilterOutDigits
import com.radenyaqien.core.navigation.Route
import com.radenyaqien.core.util.UiEvent
import com.radenyaqien.onboarding_domain.ValidateNutrientGoals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientsGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
    private val validateNutrientGoals: ValidateNutrientGoals
) : ViewModel() {

    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun event(event: NutrientGoalEvent) {
        when (event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {
                state = state.copy(
                    carbRatio = filterOutDigits(event.ratio)
                )
            }

            is NutrientGoalEvent.OnProteinRatioEnter -> {
                state = state.copy(
                    proteinRatio = filterOutDigits(event.ratio)
                )
            }

            is NutrientGoalEvent.OnFatRatioEnter -> {
                state = state.copy(
                    fatRatio = filterOutDigits(event.ratio)
                )
            }

            NutrientGoalEvent.OnNextClick -> {
                val validate = validateNutrientGoals(
                    carbRatioText = state.carbRatio,
                    proteinRatioText = state.proteinRatio,
                    fatRatioText = state.fatRatio
                )

                when (validate) {
                    is ValidateNutrientGoals.NutrientResult.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowMessage(validate.message)
                            )
                        }
                    }

                    is ValidateNutrientGoals.NutrientResult.Success -> {
                        preferences.saveCarbRatio(validate.carbRatio)
                        preferences.saveProteinRatio(validate.proteinRatio)
                        preferences.saveFatRatio(validate.fatRatio)
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.Navigate(Route.TRACKER_OVERVIEW))
                        }
                    }
                }
            }
        }
    }

}