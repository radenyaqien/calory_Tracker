package com.radenyaqien.tracker_data

import android.app.Application
import androidx.room.Room
import com.radenyaqien.tracker_data.local.TrackerDatabase
import com.radenyaqien.tracker_data.remote.OpenFoodApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {


    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()
    }

    @Provides
    @Singleton
    fun providesApi(okHttpClient: OkHttpClient): OpenFoodApi {
        return Retrofit.Builder()
            .baseUrl(OpenFoodApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(OpenFoodApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Application): TrackerDatabase {
        return Room.databaseBuilder(
            context,
            TrackerDatabase::class.java,
            "tracker_db"
        ).build()
    }

}