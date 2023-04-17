package com.radenyaqien.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radenyaqien.core.domain.use_case.FilterOutDigits
import com.radenyaqien.core.util.UiEvent
import com.radenyaqien.core.util.UiText
import com.radenyaqien.tracker_domain.use_case.TrackerUseCase
import com.radenyaqien.tracker_presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCase: TrackerUseCase,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {


    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnAmountForFoodChange -> {
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if (it.food == event.food) {
                            it.copy(
                                amount = filterOutDigits(event.amount)
                            )
                        } else it
                    }
                )
            }

            is SearchEvent.OnQueryChange -> {
                state = state.copy(
                    query = event.query
                )
            }

            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }

            is SearchEvent.OnToggleTrackableFood -> {
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if (it.food == event.food) {
                            it.copy(
                                isExpanded = !it.isExpanded
                            )
                        } else it
                    }
                )
            }

            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }

            SearchEvent.Onsearch -> {
                executeSearch()
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                trackableFood = emptyList()
            )
            trackerUseCase.searchFood(
                state.query, 1, 40
            ).onSuccess { foods ->
                state = state.copy(
                    trackableFood = foods.map { TrackableUiState(it) },
                    isSearching = false,
                    query = ""
                )
            }.onFailure {
                state = state.copy(isSearching = false)
                _uiEvent.send(
                    UiEvent.ShowMessage(
                        UiText.StringResource(R.string.error_something_went_wrong)
                    )
                )
            }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it.food == event.food }
            trackerUseCase.trackFood(
                food = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

}