package com.radenyaqien.tracker_domain.use_case

import com.radenyaqien.tracker_domain.model.TrackableFood
import com.radenyaqien.tracker_domain.repository.TrackerRepository

class SearchFood(
    private val trackerRepository: TrackerRepository
) {

    suspend operator fun invoke(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {

        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return trackerRepository.searchFood(query.trim(), page, pageSize)
    }

}