package com.radenyaqien.onboarding_domain

import com.radenyaqien.core.util.UiText

class ValidateNutrientGoals {

    operator fun invoke(
        carbRatioText: String,
        proteinRatioText: String,
        fatRatioText: String
    ): NutrientResult {

        val carbRatio = carbRatioText.toIntOrNull()
        val proteinRatio: Int? = proteinRatioText.toIntOrNull()
        val fatRatio: Int? = fatRatioText.toIntOrNull()

        if (carbRatio == null || proteinRatio == null || fatRatio == null) {
            return NutrientResult.Error(UiText.StringResource(R.string.error_invalid_values))
        }
        if (carbRatio + proteinRatio + fatRatio != 100) {
            return NutrientResult.Error(UiText.StringResource(R.string.error_not_100_percent))
        }
        return NutrientResult.Success(carbRatio / 100f, proteinRatio / 100f, fatRatio / 100f)
    }


    sealed class NutrientResult {
        data class Success(
            val carbRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ) : NutrientResult()

        data class Error(val message: UiText) : NutrientResult()
    }
}