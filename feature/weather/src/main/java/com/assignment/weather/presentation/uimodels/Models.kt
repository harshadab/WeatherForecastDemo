package com.assignment.weather.presentation.uimodels

data class ForecastLocationUI(
    val city: String,
    val forecast: List<ForecastDayUI>,
    val current: CurrentWeatherUI
)

data class ForecastDayUI(
    val date: String,
    val dayOfWeek: String,
    val day: DayWeatherUI,
    val hour: List<HourWeatherUI>
)

data class DayWeatherUI(
    val avgTempC: String,
    val highTempC: String,
    val lowTempC: String,
    val condition: ConditionUI
)

data class CurrentWeatherUI(
    val tempC: String,
    val feelsLikeC: String,
    val condition: ConditionUI
)

data class HourWeatherUI(
    val hourFormatted: String,
    val tempC: String,
    val condition: ConditionUI,
    val humidity: String
)

data class ConditionUI(
    val text: String,
    val icon: String
)
