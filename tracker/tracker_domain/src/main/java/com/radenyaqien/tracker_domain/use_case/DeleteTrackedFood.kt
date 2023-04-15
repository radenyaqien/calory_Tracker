package com.radenyaqien.tracker_domain.use_case

import com.radenyaqien.tracker_domain.model.TrackedFood
import com.radenyaqien.tracker_domain.repository.TrackerRepository

class DeleteTrackedFood(
    private val trackerRepository: TrackerRepository
) {

    suspend operator fun invoke(
        trackedFood: TrackedFood
    ) {
        return trackerRepository.deleteTrackedFood(trackedFood)
    }

}