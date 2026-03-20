package com.assignment.weather.data.repository

import com.assignment.core.Result
import com.assignment.core.providers.DispatcherProvider
import com.assignment.weather.data.api.ApiConfig
import com.assignment.weather.data.api.WeatherApiService
import com.assignment.weather.data.mapper.ForecastResponseMapper
import com.assignment.weather.data.models.ForecastResponseDto
import com.assignment.weather.domain.models.ForecastLocation
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherForecastRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = object : DispatcherProvider {
        override val mainDispatcher: CoroutineDispatcher = testDispatcher
        override val ioDispatcher: CoroutineDispatcher = testDispatcher
        override val defaultDispatcher: CoroutineDispatcher = testDispatcher
        override val unconfinedDispatcher: CoroutineDispatcher = testDispatcher
    }

    private lateinit var repository: WeatherForecastRepositoryImpl
    private val api = mockk<WeatherApiService>()
    private val apiConfig = mockk<ApiConfig>()
    private val mapper = mockk<ForecastResponseMapper>()
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        every { apiConfig.apiKey } returns "test_key"
        repository = WeatherForecastRepositoryImpl(api, apiConfig, mapper, dispatcherProvider, json)
    }

    @Test
    fun `getWeatherForecast returns Success when api call is successful`() = runTest(testDispatcher) {
        val city = "London"
        val days = 7
        val dto = ForecastResponseDto(null, null, null)
        val mappedLocation = ForecastLocation(null, null, null)

        coEvery { api.getForecast("test_key", city, days) } returns Response.success(dto)
        every { mapper.map(dto) } returns mappedLocation

        val result = repository.getWeatherForecast(city, days)

        assertTrue(result is Result.Success)
        assertEquals(mappedLocation, (result as Result.Success).data)
    }

    @Test
    fun `getWeatherForecast returns Error when api call is unsuccessful with error body`() = runTest(testDispatcher) {
        val city = "London"
        val days = 7
        val errorJson = """{"error":{"code":1006,"message":"No matching location found."}}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        
        coEvery { api.getForecast("test_key", city, days) } returns Response.error(400, errorBody)

        val result = repository.getWeatherForecast(city, days)

        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertEquals("No matching location found.", errorResult.message)
        assertEquals(1006, errorResult.code)
    }

    @Test
    fun `getWeatherForecast returns Error for IOException`() = runTest(testDispatcher) {
        val city = "London"
        val days = 7
        
        coEvery { api.getForecast("test_key", city, days) } throws IOException()

        val result = repository.getWeatherForecast(city, days)

        assertTrue(result is Result.Error)
        assertEquals("Network error", (result as Result.Error).message)
    }

    @Test
    fun `getWeatherForecast returns Error when response body is null`() = runTest(testDispatcher) {
        val city = "London"
        val days = 7
        
        coEvery { api.getForecast("test_key", city, days) } returns Response.success(null)

        val result = repository.getWeatherForecast(city, days)

        assertTrue(result is Result.Error)
        assertEquals("Empty response body", (result as Result.Error).message)
    }
}
