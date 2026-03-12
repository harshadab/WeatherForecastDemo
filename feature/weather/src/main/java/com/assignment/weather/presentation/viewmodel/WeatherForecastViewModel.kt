package com.assignment.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.core.Result
import com.assignment.core.providers.DispatcherProvider
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.usecases.GetWeatherForecastUC
import com.assignment.weather.presentation.uimapper.WeatherForecastUIMapper
import com.assignment.weather.presentation.uistates.ForecastEvent
import com.assignment.weather.presentation.uistates.ForecastIntent
import com.assignment.weather.presentation.uistates.WeatherForecastUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val getWeatherForecastUC: GetWeatherForecastUC,
    private val mapper: WeatherForecastUIMapper,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state =
        MutableStateFlow<WeatherForecastUIState>(WeatherForecastUIState.Loading)
    val state: StateFlow<WeatherForecastUIState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<ForecastEvent>()
    val events: SharedFlow<ForecastEvent> = _events.asSharedFlow()

    private val searchQuery = MutableStateFlow(DEFAULT_CITY)

    init {
        observeSearch()
    }

    /**
     * Observe search input with debounce
     */
    private fun observeSearch() {
        viewModelScope.launch(dispatcherProvider.mainDispatcher) {
            searchQuery
                .debounce(DEBOUNCE_DELAY)
                .distinctUntilChanged()
                .collectLatest { city ->
                    loadWeather(city)
                }
        }
    }

    /**
     * Handle UI intents
     */
    fun onIntent(intent: ForecastIntent) {
        when (intent) {

            is ForecastIntent.SearchCity -> {
                searchQuery.value = intent.city
            }

            ForecastIntent.Refresh -> {
                loadWeather(searchQuery.value)
            }

            ForecastIntent.CurrentLocation -> {
                viewModelScope.launch {
                    _events.emit(ForecastEvent.ShowCurrentLocationWeather)
                }
            }
        }
    }

    /**
     * Load weather data
     */
    private fun loadWeather(city: String) {

        if (city.isBlank()) return

        viewModelScope.launch(dispatcherProvider.mainDispatcher) {

            _state.value = WeatherForecastUIState.Loading

            val result = withContext(dispatcherProvider.ioDispatcher) {
                getWeatherForecastUC.execute(city)
            }

            when (result) {

                is Result.Success<ForecastLocation> -> {
                    _state.value =
                        WeatherForecastUIState.Success(
                            mapper.map(result.data)
                        )
                }

                is Result.Error -> {

                    _state.value =
                        WeatherForecastUIState.Error(result.message)

                    _events.emit(
                        ForecastEvent.ShowError(result.message)
                    )
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_CITY = "Stockholm"
        private const val DEBOUNCE_DELAY = 500L
    }
}

