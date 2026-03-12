package com.assignment.weather.domain.repository

import com.assignment.core.Result
import com.assignment.weather.domain.models.ForecastLocation

interface WeatherForecastRepository {
    suspend fun getWeatherForecast(city: String, days: Int): Result<ForecastLocation>
}