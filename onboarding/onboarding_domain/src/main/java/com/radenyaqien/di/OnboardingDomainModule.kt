package com.radenyaqien.di

import com.radenyaqien.onboarding_domain.ValidateNutrientGoals
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideNutrientGoalsValidation(): ValidateNutrientGoals {
        return ValidateNutrientGoals()
    }
}