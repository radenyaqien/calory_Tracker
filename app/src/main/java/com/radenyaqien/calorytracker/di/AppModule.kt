package com.radenyaqien.calorytracker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.radenyaqien.core.domain.DefaultPreferences
import com.radenyaqien.core.domain.preferences.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(context: Application): SharedPreferences {
        return context.getSharedPreferences("shared_preff", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return DefaultPreferences(sharedPreferences)
    }


}