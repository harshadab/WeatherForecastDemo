package com.assignment.weather.presentation.uimapper

import com.assignment.core.mapper.Mapper
import com.assignment.core.utils.getFullUrl
import com.assignment.core.utils.orDefault
import com.assignment.core.utils.roundToString
import com.assignment.core.utils.toDayOfWeek
import com.assignment.core.utils.toHour
import com.assignment.weather.domain.models.Condition
import com.assignment.weather.domain.models.CurrentWeather
import com.assignment.weather.domain.models.Day
import com.assignment.weather.domain.models.Forecast
import com.assignment.weather.domain.models.ForecastDay
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.models.Hour
import com.assignment.weather.presentation.uimodels.ConditionUI
import com.assignment.weather.presentation.uimodels.CurrentWeatherUI
import com.assignment.weather.presentation.uimodels.DayWeatherUI
import com.assignment.weather.presentation.uimodels.ForecastDayUI
import com.assignment.weather.presentation.uimodels.ForecastLocationUI
import com.assignment.weather.presentation.uimodels.HourWeatherUI
import javax.inject.Inject

class WeatherForecastUIMapper @Inject constructor() : Mapper<ForecastLocation, ForecastLocationUI> {

    override fun map(data: ForecastLocation): ForecastLocationUI {
        return ForecastLocationUI(
            city = data.location?.name.orDefault(),
            forecast = mapForecast(data.forecast),
            current = mapCurrentWeather(data.current)
        )
    }

    private fun mapForecast(forecast: Forecast?) =
        forecast?.forecastDay?.map {
            mapForecastDay(it)
        } ?: emptyList()

    private fun mapForecastDay(day: ForecastDay?): ForecastDayUI =
        ForecastDayUI(
            date = day?.date.orDefault(),
            dayOfWeek = day?.dateEpoch.toDayOfWeek(),
            day = mapDay(day?.day),
            hour = day?.hour?.map { mapHour(it) } ?: emptyList()
        )

    private fun mapDay(day: Day?): DayWeatherUI {
        return DayWeatherUI(
            avgTempC = day?.avgTempC.roundToString(),
            highTempC = day?.maxTempC.roundToString(),
            lowTempC = day?.minTempC.roundToString(),
            condition = mapCondition(day?.condition)
        )
    }

    private fun mapHour(hour: Hour?): HourWeatherUI {
        return HourWeatherUI(
            hourFormatted = hour?.timeEpoch.toHour(),
            tempC = hour?.tempC.roundToString(),
            condition = mapCondition(hour?.condition),
            humidity = hour?.humidity?.toString().orDefault()
        )
    }

    private fun mapCurrentWeather(current: CurrentWeather?): CurrentWeatherUI {
        return CurrentWeatherUI(
            tempC = current?.tempC.roundToString(),
            feelsLikeC = current?.feelsLikeC.roundToString(),
            condition = mapCondition(current?.condition)
        )
    }

    private fun mapCondition(condition: Condition?): ConditionUI {
        return ConditionUI(
            text = condition?.text.orDefault(),
            icon = condition?.icon.getFullUrl()
        )
    }
}
