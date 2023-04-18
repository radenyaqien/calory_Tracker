package com.radenyaqien.calorytracker.repository

import com.radenyaqien.tracker_domain.model.TrackableFood
import com.radenyaqien.tracker_domain.model.TrackedFood
import com.radenyaqien.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import kotlin.random.Random

class TrackerRepositoryFake : TrackerRepository {
    var shouldReturnError = false
    val trackedFoods = mutableListOf<TrackedFood>()
    var results = listOf<TrackableFood>()
    private val getFoodsForDateFlow =
        MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchFood(
        query: String,
        page: Int,
        page_size: Int
    ): Result<List<TrackableFood>> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(results)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackedFoods.add(food.copy(id = Random.nextInt()))
        getFoodsForDateFlow.emit(trackedFoods)
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackedFoods.remove(food)
        getFoodsForDateFlow.emit(trackedFoods)
    }

    override fun getFoodForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}