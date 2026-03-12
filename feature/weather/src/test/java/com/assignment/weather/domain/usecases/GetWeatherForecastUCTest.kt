package com.assignment.weather.domain.usecases

import com.assignment.core.Result
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.repository.WeatherForecastRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetWeatherForecastUCTest {

    private lateinit var useCase: GetWeatherForecastUCImpl
    private val repository = mockk<WeatherForecastRepository>()

    @Before
    fun setUp() {
        useCase = GetWeatherForecastUCImpl(repository)
    }

    @Test
    fun `execute returns Result Success from repository`() = runTest {
        val city = "London"
        val expectedResult = Result.Success(ForecastLocation(null, null, null))
        coEvery { repository.getWeatherForecast(city, 7) } returns expectedResult

        val actualResult = useCase.execute(city)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `execute returns Result Error from repository`() = runTest {
        val city = "London"
        val expectedResult = Result.Error("Error message")
        coEvery { repository.getWeatherForecast(city, 7) } returns expectedResult

        val actualResult = useCase.execute(city)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `execute with custom days returns expected Result`() = runTest {
        val city = "London"
        val days = 3
        val expectedResult = Result.Success(ForecastLocation(null, null, null))
        coEvery { repository.getWeatherForecast(city, days) } returns expectedResult

        val actualResult = useCase.execute(city, days)

        assertEquals(expectedResult, actualResult)
    }
}
