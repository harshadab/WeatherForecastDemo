package com.assignment.weatherforecastdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.assignment.weather.presentation.screens.WeatherForecastScreen
import com.assignment.weather.presentation.viewmodel.WeatherForecastViewModel
import com.assignment.core.location_helper.LocationHelper
import com.assignment.weatherforecastdemo.ui.theme.WeatherForecastDemoTheme
import com.assignment.core.uicomponents.views.LocationPermission
import com.assignment.weather.presentation.uistates.ForecastEvent
import com.assignment.weather.presentation.uistates.ForecastIntent
import com.assignment.weather.presentation.screens.SearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class WeatherForecastActivity : ComponentActivity() {

    private val weatherForecastViewModel: WeatherForecastViewModel by viewModels()

    @Inject
    lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true

        setContent {
            WeatherForecastDemoTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                CollectOneTimeEvents(weatherForecastViewModel, snackbarHostState)
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),

                    topBar = {
                        SearchBar(
                            onQueryChange = {
                                weatherForecastViewModel.onIntent(ForecastIntent.SearchCity(it))
                            }, onCurrentLocationClick = {
                                weatherForecastViewModel.onIntent(ForecastIntent.CurrentLocation)
                            })
                    }

                ) { innerPadding ->

                    LocationPermission(locationHelper) {
                        weatherForecastViewModel.onIntent(ForecastIntent.SearchCity(it))
                    }
                    val state by weatherForecastViewModel.state.collectAsStateWithLifecycle()

                    WeatherForecastScreen(
                        state,
                        { weatherForecastViewModel.onIntent(ForecastIntent.Refresh) },
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }

    @Composable
    private fun CollectOneTimeEvents(
        viewModel: WeatherForecastViewModel,
        snackbarHostState: SnackbarHostState
    ) {
        var showPermissionDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is ForecastEvent.ShowError -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }

                    ForecastEvent.ShowCurrentLocationWeather -> showPermissionDialog = true
                }
            }
        }

        if (showPermissionDialog) {
            LocationPermission(locationHelper) { city ->
                viewModel.onIntent(ForecastIntent.SearchCity(city))
                showPermissionDialog = false
            }
        }
    }
}
