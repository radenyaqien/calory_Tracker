package com.radenyaqien.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import com.radenyaqien.core_ui.CarbColor
import com.radenyaqien.core_ui.FatColor
import com.radenyaqien.core_ui.ProteinColor

@Composable
fun NutrientBar(
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    calorieGoal: Int,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colors.background
    val caloriesExceedColor = MaterialTheme.colors.error
    val carbsWidthratio = remember {
        AnimationState(0f)
    }
    val proteinWidthratio = remember {
        AnimationState(0f)
    }
    val fatWidthratio = remember {
        AnimationState(0f)
    }

    LaunchedEffect(key1 = carbs) {
        carbsWidthratio.animateTo(
            targetValue = (carbs * 4f) / calorieGoal
        )
    }
    LaunchedEffect(key1 = protein) {
        proteinWidthratio.animateTo(
            targetValue = (protein * 4f) / calorieGoal
        )
    }
    LaunchedEffect(key1 = carbs) {
        fatWidthratio.animateTo(
            targetValue = (fat * 9f) / calorieGoal
        )
    }

    Canvas(modifier = modifier) {
        if (calories <= calorieGoal) {
            val carbsWidth = carbsWidthratio.value * size.width
            val proteinWidth = proteinWidthratio.value * size.width
            val fatWidth = fatWidthratio.value * size.width
            drawRoundRect(
                color = background,
                size = size,
                cornerRadius = CornerRadius(100f)
            )
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )
            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )
            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )
        } else {
            drawRoundRect(
                color = caloriesExceedColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )
        }
    }

}