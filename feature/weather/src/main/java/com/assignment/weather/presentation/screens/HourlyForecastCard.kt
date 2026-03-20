package com.assignment.weather.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.assignment.core.R
import com.assignment.core.uicomponents.common.Dimens
import com.assignment.core.uicomponents.views.GlassCard
import com.assignment.core.uicomponents.common.TypographyDefault
import com.assignment.core.utils.formatHourLabel
import com.assignment.weather.presentation.uimodels.ConditionUI
import com.assignment.weather.presentation.uimodels.DayWeatherUI
import com.assignment.weather.presentation.uimodels.ForecastDayUI
import com.assignment.weather.presentation.uimodels.HourWeatherUI
import java.time.LocalTime


@Composable
fun HourlyForecastCard(day: ForecastDayUI) {

    val listState = rememberLazyListState()

    val currentHour = LocalTime.now().hour

        val startIndex = remember(day.hour) {
        day.hour.indexOfFirst {
            it.hourFormatted.substringBefore(":").toInt() == currentHour
        }.coerceAtLeast(0)
    }

    LaunchedEffect(day) {
        listState.scrollToItem(startIndex)
    }

    GlassCard {

        Text(
            text = stringResource(id = R.string.hourly_forecast_title),
            color = Color.White,
            style = TypographyDefault().fontTitle2
        )

        Spacer(Modifier.height(Dimens.spaceM))

        LazyRow(
            state = listState,
        ) {
            itemsIndexed(day.hour) { index, hour ->

                HourlyForecastItem(
                    hour = hour,
                )
            }
        }
    }
}

@Composable
fun HourlyForecastItem(
    hour: HourWeatherUI
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(Dimens.hourCardItemHeight)
    ) {

        Text(
            text = formatHourLabel(hour.hourFormatted),
            color = Color.White
        )

        Spacer(Modifier.height(Dimens.spaceS))

        WeatherIcon(
            iconUrl = hour.condition.icon,
            modifier = Modifier.size(Dimens.iconSizeLarge)
        )

        Spacer(Modifier.height(Dimens.spaceS))

        Text(
            text = stringResource(id = R.string.temperature_degree_format, hour.tempC),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.percent_format, hour.humidity),
            color = Color.White.copy(.6f),
            style = TypographyDefault().fontCaption1
        )
    }
}

@Preview
@Composable
fun HourlyForecastCardPreview() {
    val current = ForecastDayUI(
        date = "2024-05-21",
        dayOfWeek = "Tuesday",
        day = DayWeatherUI(
            avgTempC = "18",
            highTempC = "25",
            lowTempC = "12",
            condition = ConditionUI(
                text = "Partly cloudy",
                icon = "https://cdn.weatherapi.com/weather/64x64/day/116.png"
            )
        ),
        hour = listOf(
            HourWeatherUI(
                hourFormatted = "12:00",
                tempC = "17",
                condition = ConditionUI(
                    text = "Sunny",
                    icon = "//https:cdn.weatherapi.com/weather/64x64/day/113.png"
                ),
                humidity = "60"
            ),
            HourWeatherUI(
                hourFormatted = "15:00",
                tempC = "19",
                condition = ConditionUI(
                    text = "Sunny",
                    icon = "//https:cdn.weatherapi.com/weather/64x64/day/113.png"
                ),
                humidity = "55"
            )
        )
    )
    HourlyForecastCard(current)
}
