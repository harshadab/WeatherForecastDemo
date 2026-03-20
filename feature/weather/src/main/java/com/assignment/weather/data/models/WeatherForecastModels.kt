package com.assignment.weather.data.models
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseDto(
    val location: LocationDto?,
    val forecast: ForecastDto?,
    val current: CurrentWeatherDto?
)

@Serializable
data class LocationDto(
    val name: String = "",
    val lat: Double?,
    val lon: Double?
)

@Serializable
data class ForecastDto(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDayDto?>?
)

@Serializable
data class ForecastDayDto(
    val date: String = "",
    @SerialName("date_epoch")
    val dateEpoch: Int,
    val day: DayDto?,
    val hour: List<HourDto?>?
)

@Serializable
data class HourDto(
    @SerialName("time_epoch")
    val timeEpoch: Int?,
    @SerialName("temp_c")
    val tempC: Double?,
    @SerialName("condition")
    val condition: ConditionDto,
    val humidity: Int?
)
@Serializable
data class DayDto(
    @SerialName("avgtemp_c")
    val avgTempC: Double?,
    @SerialName("maxtemp_c")
    val maxTempC: Double?,
    @SerialName("mintemp_c")
    val minTempC: Double?,
    @SerialName("condition")
    val condition: ConditionDto?
)
@Serializable
data class CurrentWeatherDto(
    @SerialName("temp_c")
    val tempC: Double?,
    @SerialName("feelslike_c")
    val feelsLikeC: Double?,
    @SerialName("condition")
    val condition: ConditionDto?
)

@Serializable
data class ConditionDto(
    @SerialName("text")
    val text: String = "",
    val icon: String = ""
)

@Serializable
data class ErrorResponse(
    @SerialName("error")
    val error: Error
)

@Serializable
data class Error(
    val code: Int?,
    val message: String = ""
)
