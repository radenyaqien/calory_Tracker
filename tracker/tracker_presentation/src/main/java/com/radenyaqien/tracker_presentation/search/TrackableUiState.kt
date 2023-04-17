package com.radenyaqien.tracker_presentation.search

import com.radenyaqien.tracker_domain.model.TrackableFood

data class TrackableUiState(
    val food: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)
