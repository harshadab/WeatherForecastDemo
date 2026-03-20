package com.assignment.weather.presentation.uimapper

import com.assignment.core.utils.toDayOfWeek
import com.assignment.core.utils.toHour
import com.assignment.weather.domain.models.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherForecastUIMapperTest {

    private lateinit var mapper: WeatherForecastUIMapper

    @Before
    fun setUp() {
        mapper = WeatherForecastUIMapper()
    }

    @Test
    fun `map correctly transforms ForecastLocation to ForecastLocationUI`() {
        val domainModel = ForecastLocation(
            location = Location(name = "London", lat = 51.52, lon = -0.11),
            current = CurrentWeather(
                tempC = 15.4,
                feelsLikeC = 14.2,
                condition = Condition(text = "Clear", icon = "//cdn.weatherapi.com/weather/64x64/day/113.png")
            ),
            forecast = Forecast(
                forecastDay = listOf(
                    ForecastDay(
                        date = "2023-10-10",
                        dateEpoch = 1696896000,
                        day = Day(
                            avgTempC = 12.3,
                            maxTempC = 16.8,
                            minTempC = 10.1,
                            condition = Condition(text = "Sunny", icon = "//icon")
                        ),
                        hour = listOf(
                            Hour(
                                timeEpoch = 1696896000,
                                tempC = 11.2,
                                condition = Condition(text = "Clear", icon = "//icon"),
                                humidity = 80
                            )
                        )
                    )
                )
            )
        )

        val result = mapper.map(domainModel)

        // Location
        assertEquals("London", result.city)

        // Current
        assertEquals("15", result.current.tempC)
        assertEquals("14", result.current.feelsLikeC)
        assertEquals("Clear", result.current.condition?.text)
        assertEquals("https://cdn.weatherapi.com/weather/64x64/day/113.png", result.current.condition?.icon)

        // Forecast
        assertEquals(1, result.forecast.size)
        val firstDay = result.forecast.first()
        assertEquals("2023-10-10", firstDay.date)
        val expectedDayOfWeek = 1696896000.toDayOfWeek()
        assertEquals(expectedDayOfWeek, firstDay.dayOfWeek)
        assertEquals("12", firstDay.day?.avgTempC)
        assertEquals("17", firstDay.day?.highTempC) // 16.8 rounded up
        assertEquals("10", firstDay.day?.lowTempC)

        // Hour
        assertEquals(1, firstDay.hour.size)
        assertEquals("80", firstDay.hour.first().humidity)
        val expectedHourFormatted = 1696896000.toHour()
        assertEquals(expectedHourFormatted, firstDay.hour.first().hourFormatted)
        assertEquals("11", firstDay.hour.first().tempC)
    }

    @Test
    fun `map handles null values in domain model`() {
        val domainModel = ForecastLocation(
            location = null,
            current = null,
            forecast = null
        )

        val result = mapper.map(domainModel)

        assertEquals("", result.city)
        assertEquals("", result.current.tempC)
        assertEquals("", result.current.feelsLikeC)
        assertEquals("", result.current.condition?.text)
        assertEquals("", result.current.condition?.icon)
        assertEquals(0, result.forecast.size)
    }
}
