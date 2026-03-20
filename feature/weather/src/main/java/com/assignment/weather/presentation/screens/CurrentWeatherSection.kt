package com.assignment.weather.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.assignment.core.R
import com.assignment.core.uicomponents.common.Dimens
import com.assignment.weather.presentation.uimodels.ConditionUI
import com.assignment.weather.presentation.uimodels.CurrentWeatherUI
import com.assignment.core.uicomponents.common.TypographyDefault

@Composable
fun CurrentWeatherSection(current: CurrentWeatherUI) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Text(
                text = stringResource(id = R.string.temperature_degree_format, current.tempC),
                style = TypographyDefault().fontSuperLarge,
                color = Color.White,
            )

            Text(
                text = stringResource(id = R.string.feels_like_format, current.feelsLikeC),
                style = TypographyDefault().fontBody,
                color = Color.White.copy(alpha = 0.8f)
            )

            Text(
                text = current.condition.text,
                style = TypographyDefault().fontBody,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        WeatherIcon(
            iconUrl = current.condition.icon,
            modifier = Modifier.size(Dimens.weatherIconSize)
        )
    }
}

@Composable
fun WeatherIcon(iconUrl: String, modifier: Modifier = Modifier) {
    if (iconUrl.isEmpty()) return
    val imageRequest = ImageRequest
        .Builder(LocalContext.current)
        .data(iconUrl)
        .crossfade(true)
        .build()
    AsyncImage(
        model = imageRequest,
        contentDescription = stringResource(id = R.string.weather_icon_content_description),
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Preview
@Composable
fun CurrentWeatherSectionPreview() {
    val current = CurrentWeatherUI(
        tempC = "15",
        feelsLikeC = "14",
        condition = ConditionUI(
            text = "Partly cloudy",
            icon = "https://cdn.weatherapi.com/weather/64x64/day/116.png"
        )
    )
    CurrentWeatherSection(current)
}
