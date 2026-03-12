package com.assignment.weatherforecastdemo.api_config

import com.assignment.weather.data.api.ApiConfig
import com.assignment.weatherforecastdemo.BuildConfig
import javax.inject.Inject

class ApiConfigImpl @Inject constructor() : ApiConfig {
    override val apiKey: String = BuildConfig.WEATHER_API_KEY
}
