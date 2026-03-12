package com.assignment.weather.presentation.uistates

sealed class ForecastEvent {
    data class ShowError(val message: String) : ForecastEvent()
    data object ShowCurrentLocationWeather : ForecastEvent()
}