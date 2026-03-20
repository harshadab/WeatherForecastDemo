package com.assignment.weather.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.assignment.weather.presentation.uimodels.ConditionUI
import com.assignment.weather.presentation.uimodels.CurrentWeatherUI
import com.assignment.weather.presentation.uimodels.DayWeatherUI
import com.assignment.weather.presentation.uimodels.ForecastDayUI
import com.assignment.weather.presentation.uimodels.ForecastLocationUI
import com.assignment.weather.presentation.uimodels.HourWeatherUI
import com.assignment.weather.presentation.uistates.WeatherForecastUIState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class WeatherForecastScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsCircularProgressIndicator() {
        composeTestRule.setContent {
            WeatherForecastScreen(
                state = WeatherForecastUIState.Loading,
                onRefresh = {},
                innerPadding = PaddingValues()
            )
        }

        composeTestRule.onNodeWithTag("circularProgressIndicator").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorText() {
        composeTestRule.setContent {
            WeatherForecastScreen(
                state = WeatherForecastUIState.Error("Some error"),
                onRefresh = {},
                innerPadding = PaddingValues()
            )
        }

        composeTestRule.onNodeWithTag("errorText")
            .assertExists()
    }

    @Test
    fun successState_showsWeatherDetails() {
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

        composeTestRule.setContent {
            WeatherForecastScreen(
                state = sampleData,
                onRefresh = {},
                innerPadding = PaddingValues()
            )
        }

        composeTestRule.onNodeWithText("New York").assertIsDisplayed()
        composeTestRule.onNodeWithText("15°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sunny").assertIsDisplayed()
    }
}
