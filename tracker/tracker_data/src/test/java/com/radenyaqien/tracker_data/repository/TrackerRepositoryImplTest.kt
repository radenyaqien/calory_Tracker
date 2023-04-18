package com.radenyaqien.tracker_data.repository

import com.google.common.truth.Truth
import com.radenyaqien.tracker_data.remote.OpenFoodApi
import com.radenyaqien.tracker_data.remote.invalidFoodResponse
import com.radenyaqien.tracker_data.remote.validFoodResponse
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TrackerRepositoryImplTest {

    private lateinit var repository: TrackerRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: OpenFoodApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(OpenFoodApi::class.java)
        repository = TrackerRepositoryImpl(
            dao = mockk(relaxed = true),
            api = api
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Search food valid Response, return result`() = runBlocking {
        mockWebServer.enqueue(
            response = MockResponse()
                .setResponseCode(200)
                .setBody(validFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)

        Truth.assertThat(result.isSuccess).isTrue()
    }


    @Test
    fun `Search food invalid Response, return failure`() = runBlocking {
        mockWebServer.enqueue(
            response = MockResponse()
                .setResponseCode(403)
                .setBody(validFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)

        Truth.assertThat(result.isFailure).isTrue()
    }
    @Test
    fun `Search food malformed response, return failure`() = runBlocking {
        mockWebServer.enqueue(
            response = MockResponse()

                .setBody(invalidFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)

        Truth.assertThat(result.isFailure).isTrue()
    }
}