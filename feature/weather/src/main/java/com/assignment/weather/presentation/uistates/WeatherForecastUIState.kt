package com.assignment.weather.presentation.uistates

import com.assignment.weather.presentation.uimodels.ForecastLocationUI

sealed class WeatherForecastUIState {
    data object Loading : WeatherForecastUIState()
    data class Success(val data: ForecastLocationUI) : WeatherForecastUIState()
    data class Error(val message: String) : WeatherForecastUIState()
}