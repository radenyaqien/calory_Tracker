package com.radenyaqien.tracker_domain.di

import com.radenyaqien.core.domain.preferences.Preferences
import com.radenyaqien.tracker_domain.repository.TrackerRepository
import com.radenyaqien.tracker_domain.use_case.CalculateMealNutrients
import com.radenyaqien.tracker_domain.use_case.DeleteTrackedFood
import com.radenyaqien.tracker_domain.use_case.GetFoodsForDate
import com.radenyaqien.tracker_domain.use_case.SearchFood
import com.radenyaqien.tracker_domain.use_case.TrackFood
import com.radenyaqien.tracker_domain.use_case.TrackerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @Provides
    @ViewModelScoped
    fun provideTrackerUsecase(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCase {
        return TrackerUseCase(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }


}