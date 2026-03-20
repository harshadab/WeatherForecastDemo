package com.assignment.weatherforecastdemo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.assertIsDisplayed
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.assignment.weather.presentation.screens.WeatherForecastScreen
import com.assignment.weather.presentation.uistates.WeatherForecastUIState

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class WeatherForecastActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: androidx.test.rule.GrantPermissionRule =
        androidx.test.rule.GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<WeatherForecastActivity>()

    @Test
    fun searchBar_isDisplayedAndAllowsInput() {
        // Assert search bar is displayed
        composeTestRule.onNodeWithText("Search City").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search City").performTextInput("Stockholm")
        composeTestRule.onNodeWithText("Stockholm").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stockholm").performImeAction()

    }

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
    fun currentLocationButton_isDisplayedAndClickable() {
        val locationIconNode = composeTestRule.onNodeWithContentDescription("Location", ignoreCase = true)
        locationIconNode.assertIsDisplayed()
        locationIconNode.performClick()
    }
}
