package com.assignment.weather.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.assignment.core.R
import com.assignment.core.uicomponents.common.Dimens
import com.assignment.core.uicomponents.common.TypographyDefault
import com.assignment.core.uicomponents.progressbar.CircularProgressIndicatorBar
import com.assignment.core.utils.isDayTime
import com.assignment.weather.presentation.uimodels.ConditionUI
import com.assignment.weather.presentation.uimodels.CurrentWeatherUI
import com.assignment.weather.presentation.uimodels.DayWeatherUI
import com.assignment.weather.presentation.uimodels.ForecastDayUI
import com.assignment.weather.presentation.uimodels.ForecastLocationUI
import com.assignment.weather.presentation.uimodels.HourWeatherUI
import com.assignment.weather.presentation.uistates.WeatherForecastUIState

@Composable
fun WeatherForecastScreen(
    state: WeatherForecastUIState,
    onRefresh: () -> Unit,
    innerPadding: PaddingValues
) {
    WeatherForecastScreenContent(state, innerPadding, onRefresh = { onRefresh() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForecastScreenContent(
    state: WeatherForecastUIState,
    innerPadding: PaddingValues,
    onRefresh: () -> Unit,
) {

    val backgroundColor = if (isDayTime()) {
        listOf(
            Color(0xFF4FACFE),
            Color(0xFF74C0FC),
            Color(0xFFE3F2FD)
        )
    } else {
        listOf(
            Color(0xFF0B1D3A),
            Color(0xFF1F3B73),
            Color(0xFF2F5EA8)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(backgroundColor)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        var refreshing by remember { mutableStateOf(false) }

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            isRefreshing = refreshing,
            onRefresh = {
                refreshing = true
                onRefresh()
            }
        ) {
            val scrollState = rememberScrollState()
            refreshing = false
            when (state) {
                is WeatherForecastUIState.Loading -> {
                    CircularProgressIndicatorBar()
                }

                is WeatherForecastUIState.Error -> {
                    Text(
                        modifier = Modifier
                            .testTag("errorText")
                            .align(Alignment.Center),
                        text = stringResource(id = R.string.error_text),
                        textAlign = TextAlign.Center,
                        style = TypographyDefault().fontTitle2,
                        color = Color.White,

                        )
                }

                is WeatherForecastUIState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.spaceS)
                            .verticalScroll(scrollState)
                            .align(Alignment.Center)
                    ) {

                        WeatherMainContent(state.data)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherMainContent(data: ForecastLocationUI) {
    var selectedDay by remember { mutableStateOf(data.forecast.first()) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceM)
    ) {

        Spacer(Modifier.height(Dimens.spaceS))

        LocationHeader(data.city)

        Spacer(Modifier.height(Dimens.spaceS))

        CurrentWeatherSection(data.current)

        Spacer(Modifier.height(Dimens.spaceXL))

        HourlyForecastCard(selectedDay)

        Spacer(Modifier.height(Dimens.spaceM))

        WeeklyForecastCard(
            days = data.forecast,
            onDaySelected = {
                selectedDay = it
            }
        )

        Spacer(Modifier.height(Dimens.spaceXL))
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    val sampleData = WeatherForecastUIState.Success(
        ForecastLocationUI(
            city = "New York",
            current = CurrentWeatherUI(
                tempC = "15",
                feelsLikeC = "14",
                condition = ConditionUI(
                    text = "Sunny",
                    icon = "https://cdn.weatherapi.com/weather/64x64/day/113.png"
                ),
            ),
            forecast = listOf(
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
        )
    )
    WeatherForecastScreenContent(sampleData, PaddingValues(), {})
}

