package com.radenyaqien.tracker_data.repository

import com.radenyaqien.tracker_data.local.TrackerDao
import com.radenyaqien.tracker_data.mapper.toTrackableFood
import com.radenyaqien.tracker_data.remote.OpenFoodApi
import com.radenyaqien.tracker_domain.model.TrackableFood
import com.radenyaqien.tracker_domain.model.TrackedFood
import com.radenyaqien.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val api: OpenFoodApi,
    private val dao: TrackerDao
) : TrackerRepository {
    override suspend fun searchFood(
        query: String,
        page: Int,
        page_size: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(query, page, page_size)
            Result.success(
                searchDto
                    .products
                    .filter {
                        val calculatedCalories =
                            it.nutriments.carbohydrates100g * 4f +
                                    it.nutriments.proteins100g * 4f +
                                    it.nutriments.fat100g * 9f
                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f
                        it.nutriments.energyKcal100g in (lowerBound..upperBound)
                    }.mapNotNull { it.toTrackableFood() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodForDate(
            localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map {
            it.map { entity ->
                entity.toTrackedFood()
            }
        }
    }
}