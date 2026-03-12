package com.assignment.weather.data.api

import com.assignment.weather.data.models.ForecastResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 1
    ): Response<ForecastResponseDto>

}
