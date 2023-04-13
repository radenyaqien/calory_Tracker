package com.radenyaqien.calorytracker.navigation

import androidx.navigation.NavController
import com.radenyaqien.core.util.UiEvent

fun NavController.navigate(uiEvent: UiEvent.Navigate) {
    this.navigate(uiEvent.route)
}