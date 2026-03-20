package com.assignment.weather.data.mapper

import com.assignment.core.mapper.Mapper
import com.assignment.core.utils.emptyString
import com.assignment.weather.data.models.ForecastDayDto
import com.assignment.weather.data.models.ForecastDto
import com.assignment.weather.data.models.ForecastResponseDto
import com.assignment.weather.data.models.HourDto
import com.assignment.weather.data.models.LocationDto
import com.assignment.weather.data.models.ConditionDto
import com.assignment.weather.data.models.CurrentWeatherDto
import com.assignment.weather.data.models.DayDto
import com.assignment.weather.domain.models.Condition
import com.assignment.weather.domain.models.CurrentWeather
import com.assignment.weather.domain.models.Day
import com.assignment.weather.domain.models.Forecast
import com.assignment.weather.domain.models.ForecastDay
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.models.Hour
import com.assignment.weather.domain.models.Location

import javax.inject.Inject

class ForecastResponseMapper @Inject constructor(): Mapper<ForecastResponseDto, ForecastLocation> {

    override fun map(data: ForecastResponseDto): ForecastLocation {
        return ForecastLocation(
            location = data.location?.let { mapLocation(it) },
            forecast = data.forecast?.let { mapForecast(it) },
            current = mapCurrentWeather(data.current)
        )
    }

    private fun mapLocation(dto: LocationDto): Location {
        return Location(
            name = dto.name,
            lat = dto.lat,
            lon = dto.lon
        )
    }

    private fun mapForecast(dto: ForecastDto): Forecast {
        return Forecast(
            forecastDay = dto.forecastDay?.map { it?.let { mapForecastDay(it) } }
        )
    }

    private fun mapForecastDay(dto: ForecastDayDto): ForecastDay {
        return ForecastDay(
            date = dto.date,
            dateEpoch = dto.dateEpoch,
            day = dto.day?.let { mapDay(it) },
            hour = dto.hour?.map { it?.let { mapHour(it) } }
        )
    }

    private fun mapDay(dto: DayDto): Day {
        return Day(
            avgTempC = dto.avgTempC,
            maxTempC = dto.maxTempC,
            minTempC = dto.minTempC,
            condition = mapCondition(dto.condition)
        )
    }

    private fun mapHour(dto: HourDto): Hour {
        return Hour(
            timeEpoch = dto.timeEpoch,
            tempC = dto.tempC,
            condition = mapCondition(dto.condition),
            humidity = dto.humidity
        )
    }

    private fun mapCurrentWeather(dto: CurrentWeatherDto?): CurrentWeather {
        return CurrentWeather(
            tempC = dto?.tempC,
            feelsLikeC = dto?.feelsLikeC,
            condition = mapCondition(dto?.condition)
        )
    }

    private fun mapCondition(dto: ConditionDto?): Condition {
        return Condition(
            text = dto?.text ?: emptyString(),
            icon = dto?.icon ?: emptyString()
        )
    }
}
