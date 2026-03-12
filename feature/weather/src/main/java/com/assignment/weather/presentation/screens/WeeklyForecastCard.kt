package com.assignment.weather.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.core.R
import com.assignment.core.uicomponents.common.Dimens
import com.assignment.core.uicomponents.views.GlassCard
import com.assignment.core.uicomponents.common.TypographyDefault
import com.assignment.weather.presentation.uimodels.ConditionUI
import com.assignment.weather.presentation.uimodels.DayWeatherUI
import com.assignment.weather.presentation.uimodels.ForecastDayUI
import com.assignment.weather.presentation.uimodels.HourWeatherUI


@Composable
fun WeeklyForecastCard(
    days: List<ForecastDayUI>,
    onDaySelected: (ForecastDayUI) -> Unit
) {

    val minTemp = days.minOf { it.day.lowTempC.toIntOrNull() ?: 0 }
    val maxTemp = days.maxOf { it.day.highTempC.toIntOrNull() ?: 0 }

    GlassCard {

        Text(
            text = stringResource(id = R.string.weekly_forecast_title),
            color = Color.White,
            style = TypographyDefault().fontTitle2
        )

        Spacer(Modifier.height(Dimens.spaceM))

        days.forEachIndexed { index, day ->

            val label =
                if (index == 0) stringResource(id = R.string.today)
                else day.dayOfWeek

            WeeklyForecastItem(
                day = day,
                dayLabel = label,
                minTemp = minTemp,
                maxTemp = maxTemp,
                onClick = { onDaySelected(day) }
            )
        }
    }
}

@Composable
fun WeeklyForecastItem(
    day: ForecastDayUI,
    dayLabel: String,
    minTemp: Int,
    maxTemp: Int,
    onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    val high = day.day.highTempC.toIntOrNull() ?: 0
    val low = day.day.lowTempC.toIntOrNull() ?: 0

    val range = (maxTemp - minTemp).takeIf { it != 0 } ?: 1

    val startFraction = (low - minTemp) / range.toFloat()
    val endFraction = (high - minTemp) / range.toFloat()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true)
            ) { onClick() }
            .padding(vertical = Dimens.spaceS),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = dayLabel,
            color = Color.White,
            modifier = Modifier.weight(1f),
            style = TypographyDefault().fontTitle3
        )

        WeatherIcon(
            iconUrl = day.day.condition.icon,
            modifier = Modifier.size(Dimens.iconSizeLarge).weight(1f)
        )

        Spacer(modifier = Modifier.width(Dimens.spaceS))

        Text(
            text = stringResource(id = R.string.temperature_degree_format, low),
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.width(Dimens.spaceS))

        Box(
            modifier = Modifier
                .weight(2f)
                .height(Dimens.spaceXS)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(Dimens.spaceXXS)
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(endFraction)
                    .fillMaxHeight()
                    .padding(start = (startFraction * 100).dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF4FC3F7),
                                Color(0xFFFFB74D)
                            )
                        ),
                        shape = RoundedCornerShape(Dimens.spaceXXS)
                    )
            )
        }

        Spacer(modifier = Modifier.width(Dimens.spaceS))

        Text(
            text = stringResource(id = R.string.temperature_degree_format, high),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = false)
@Composable
fun WeeklyForecastCardPreview() {
    val forecast = listOf(
        ForecastDayUI(
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
    )
    WeeklyForecastCard(forecast,{})
}
