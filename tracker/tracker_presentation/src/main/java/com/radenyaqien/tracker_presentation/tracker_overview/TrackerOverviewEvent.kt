package com.radenyaqien.tracker_presentation.tracker_overview

import com.radenyaqien.tracker_domain.model.TrackedFood

sealed class TrackerOverviewEvent {

    object OnNextdayClick : TrackerOverviewEvent()
    object OnPreviousdayClick : TrackerOverviewEvent()
    data class OnToggleMealClick(val meal: Meal) : TrackerOverviewEvent()
    data class OnDeleteTrackedFoodClick(val trackedFood: TrackedFood) : TrackerOverviewEvent()
    data class OnAddFoodClick(val meal: Meal) : TrackerOverviewEvent()
}
