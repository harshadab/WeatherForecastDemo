package com.assignment.weather.presentation.viewmodel

import app.cash.turbine.test
import com.assignment.core.Result
import com.assignment.core.providers.DispatcherProvider
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.usecases.GetWeatherForecastUC
import com.assignment.weather.presentation.uimapper.WeatherForecastUIMapper
import com.assignment.weather.presentation.uimodels.ForecastLocationUI
import com.assignment.weather.presentation.uistates.ForecastEvent
import com.assignment.weather.presentation.uistates.ForecastIntent
import com.assignment.weather.presentation.uistates.WeatherForecastUIState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherForecastViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: WeatherForecastViewModel
    private val getWeatherForecastUC = mockk<GetWeatherForecastUC>()
    private val mapper = mockk<WeatherForecastUIMapper>()
    private val dispatcherProvider = object : DispatcherProvider {
        override val mainDispatcher: CoroutineDispatcher = testDispatcher
        override val ioDispatcher: CoroutineDispatcher = testDispatcher
        override val defaultDispatcher: CoroutineDispatcher = testDispatcher
        override val unconfinedDispatcher: CoroutineDispatcher = testDispatcher
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock the initial search call triggered in ViewModel.init to avoid "no answer found"
        // We use any() for 'days' as it has a default value in the interface
        coEvery { getWeatherForecastUC.execute(any(), any()) } returns Result.Error("Initial Mock")
        
        viewModel = WeatherForecastViewModel(getWeatherForecastUC, mapper, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWeather emits Success state when use case returns success`() = runTest {
        val city = "Stockholm"
        val forecastLocation = ForecastLocation(null, null, null)
        val uiModel = mockk<ForecastLocationUI>()
        
        // Specify both arguments since execute has a default parameter for days
        coEvery { getWeatherForecastUC.execute(city, any()) } returns Result.Success(forecastLocation)
        every { mapper.map(forecastLocation) } returns uiModel

        viewModel.state.test {
            // Initial state from StateFlow
            assertEquals<WeatherForecastUIState>(WeatherForecastUIState.Loading, awaitItem())

            // Trigger search
            viewModel.onIntent(ForecastIntent.SearchCity(city))
            
            // Advance for debounce (500ms)
            advanceTimeBy(500L)
            runCurrent()
            advanceUntilIdle()

            // Check Success
            assertEquals<WeatherForecastUIState>(WeatherForecastUIState.Success(uiModel), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadWeather emits Error state and event when use case returns error`() = runTest {
        val city = "Stockholm"
        val errorMessage = "Network error"
        coEvery { getWeatherForecastUC.execute(city, any()) } returns Result.Error(errorMessage)

        viewModel.state.test {
            val stateTurbine = this
            viewModel.events.test {
                val eventTurbine = this
                // Initial state
                assertEquals<WeatherForecastUIState>(WeatherForecastUIState.Loading, stateTurbine.awaitItem())

                viewModel.onIntent(ForecastIntent.SearchCity(city))
                
                advanceTimeBy(500L)
                runCurrent()
                advanceUntilIdle()

                // Check Error state
                assertEquals<WeatherForecastUIState>(WeatherForecastUIState.Error(errorMessage), stateTurbine.awaitItem())

                // Check event emitted (consume potential initial error if it hasn't happened yet)
                var event = eventTurbine.awaitItem()
                if (event is ForecastEvent.ShowError && event.message == "Initial Mock") {
                    event = eventTurbine.awaitItem()
                }
                assertEquals<ForecastEvent>(ForecastEvent.ShowError(errorMessage), event)

                eventTurbine.cancelAndIgnoreRemainingEvents()
            }
            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onIntent CurrentLocation emits ShowCurrentLocationWeather event`() = runTest {
        viewModel.events.test {
            viewModel.onIntent(ForecastIntent.CurrentLocation)
            
            // Should emit immediately or after advance
            assertEquals<ForecastEvent>(ForecastEvent.ShowCurrentLocationWeather, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchQuery debounce triggers loadWeather`() = runTest {
        val city1 = "Stockholm"
        val city2 = "Gothenburg"
        val forecastLocation = ForecastLocation(null, null, null)
        val uiModel = mockk<ForecastLocationUI>()

        coEvery { getWeatherForecastUC.execute(city2, any()) } returns Result.Success(forecastLocation)
        every { mapper.map(forecastLocation) } returns uiModel

        viewModel.state.test {
            // Initial state
            assertEquals<WeatherForecastUIState>(WeatherForecastUIState.Loading, awaitItem())

            viewModel.onIntent(ForecastIntent.SearchCity(city1))
            advanceTimeBy(200L)
            
            viewModel.onIntent(ForecastIntent.SearchCity(city2))
            
            advanceTimeBy(500L) // Full debounce for city2
            runCurrent()
            advanceUntilIdle()

            // Final state should be Success for city2
            assertEquals<WeatherForecastUIState>(WeatherForecastUIState.Success(uiModel), awaitItem())
            
            // Verify city1 was not executed due to debounce
            coVerify(exactly = 0) { getWeatherForecastUC.execute(city1, any()) }
            coVerify(exactly = 1) { getWeatherForecastUC.execute(city2, any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }
}
