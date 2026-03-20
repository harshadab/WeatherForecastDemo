package com.assignment.weather.domain.models

data class ForecastLocation(
    val location: Location?,
    val forecast: Forecast?,
    val current: CurrentWeather?
)

data class Location(
    val name: String,
    val lat: Double?,
    val lon: Double?
)

data class Forecast(
    val forecastDay: List<ForecastDay?>?
)

data class ForecastDay(
    val date: String,
    val dateEpoch: Int?,
    val hour: List<Hour?>?,
    val day: Day?,
)

data class Day(
    val avgTempC: Double?,
    val maxTempC: Double?,
    val minTempC: Double?,
    val condition: Condition?
)

data class CurrentWeather(
    val tempC: Double?,
    val feelsLikeC: Double?,
    val condition: Condition?
)

data class Hour(
    val timeEpoch: Int?,
    val tempC: Double?,
    val condition: Condition?,
    val humidity: Int?
)

data class Condition(
    val text: String,
    val icon: String
)
