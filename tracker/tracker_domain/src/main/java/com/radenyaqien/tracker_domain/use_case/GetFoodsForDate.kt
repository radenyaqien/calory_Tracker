package com.radenyaqien.tracker_domain.use_case

import com.radenyaqien.tracker_domain.model.TrackedFood
import com.radenyaqien.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(
    private val trackerRepository: TrackerRepository
) {

    operator fun invoke(
        localDate: LocalDate
    ): Flow<List<TrackedFood>> {
        return trackerRepository.getFoodForDate(localDate)
    }

}