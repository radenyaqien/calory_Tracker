package com.radenyaqien.tracker_domain.use_case

import com.radenyaqien.tracker_domain.model.MealType
import com.radenyaqien.tracker_domain.model.TrackableFood
import com.radenyaqien.tracker_domain.model.TrackedFood
import com.radenyaqien.tracker_domain.repository.TrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val trackerRepository: TrackerRepository
) {

    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate
    ) {

        return trackerRepository.insertTrackedFood(
            TrackedFood(
                name = food.name,
                carbs = ((food.carbsPer100g / 100f) * amount).roundToInt(),
                protein = ((food.carbsPer100g / 100f) * amount).roundToInt(),
                fat = ((food.carbsPer100g / 100f) * amount).roundToInt(),
                calories = ((food.carbsPer100g / 100f) * amount).roundToInt(),
                imageUrl = food.imageUrl,
                mealType = mealType,
                date = date,
                amount = amount
            )
        )
    }

}