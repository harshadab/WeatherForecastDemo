package com.assignment.weather.data.mapper

import com.assignment.weather.data.models.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ForecastResponseMapperTest {

    private lateinit var mapper: ForecastResponseMapper

    @Before
    fun setUp() {
        mapper = ForecastResponseMapper()
    }

    @Test
    fun `map correctly transforms ForecastResponseDto to ForecastLocation`() {
        val dtoJson = ForecastResponseDto(
            location = LocationDto(name = "London", lat = 51.52, lon = -0.11),
            current = CurrentWeatherDto(
                tempC = 15.0,
                feelsLikeC = 14.0,
                condition = ConditionDto(text = "Clear", icon = "//cdn.weatherapi.com/weather/64x64/day/113.png")
            ),
            forecast = ForecastDto(
                forecastDay = listOf(
                    ForecastDayDto(
                        date = "2023-10-10",
                        dateEpoch = 1696896000,
                        day = DayDto(
                            avgTempC = 12.0,
                            maxTempC = 16.0,
                            minTempC = 10.0,
                            condition = ConditionDto(text = "Sunny", icon = "//icon")
                        ),
                        hour = listOf(
                            HourDto(
                                timeEpoch = 1696896000,
                                tempC = 11.0,
                                condition = ConditionDto(text = "Clear", icon = "//icon"),
                                humidity = 80
                            )
                        )
                    )
                )
            )
        )

        val result = mapper.map(dtoJson)

        // Location
        assertEquals("London", result.location?.name)
        assertEquals(51.52, result.location?.lat)
        assertEquals(-0.11, result.location?.lon)

        // Current
        assertEquals(15.0, result.current?.tempC)
        assertEquals("Clear", result.current?.condition?.text)

        // Forecast
        assertEquals(1, result.forecast?.forecastDay?.size)
        val firstDay = result.forecast?.forecastDay?.first()
        assertEquals("2023-10-10", firstDay?.date)
        assertEquals(12.0, firstDay?.day?.avgTempC)
        
        // Hour
        assertEquals(1, firstDay?.hour?.size)
        assertEquals(80, firstDay?.hour?.first()?.humidity)
    }

    @Test
    fun `map handles null values in DTO`() {
        val dtoJson = ForecastResponseDto(
            location = null,
            current = null,
            forecast = null
        )

        val result = mapper.map(dtoJson)

        assertEquals(null, result.location)
        assertEquals(null, result.current?.tempC)
        assertEquals("", result.current?.condition?.text)
        assertEquals(null, result.forecast)
    }
}
