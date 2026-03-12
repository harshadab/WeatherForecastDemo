package com.assignment.weather.presentation.uistates


sealed class ForecastIntent {
    data class SearchCity(val city: String) : ForecastIntent()
    data object CurrentLocation : ForecastIntent()
    data object Refresh : ForecastIntent()
}


