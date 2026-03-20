package com.assignment.weather.domain.usecases

import com.assignment.core.Result
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.repository.WeatherForecastRepository
import javax.inject.Inject

interface GetWeatherForecastUC {
    suspend fun execute(
        city: String,
        days: Int = 7
    ): Result<ForecastLocation>
}

class GetWeatherForecastUCImpl @Inject constructor(
    private val repository: WeatherForecastRepository
) : GetWeatherForecastUC {

    override suspend fun execute(
        city: String,
        days: Int
    ): Result<ForecastLocation> {
        return repository.getWeatherForecast(city, days)
    }
}