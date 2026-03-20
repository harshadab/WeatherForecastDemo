package com.assignment.weather.di

import com.assignment.core.providers.DispatcherProvider
import com.assignment.weather.data.api.ApiConfig
import com.assignment.weather.data.api.WeatherApiService
import com.assignment.weather.data.mapper.ForecastResponseMapper
import com.assignment.weather.data.repository.WeatherForecastRepositoryImpl
import com.assignment.weather.domain.repository.WeatherForecastRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    private const val BASE_URL = "https://api.weatherapi.com/"

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()


    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApiService,
        apiConfig: ApiConfig,
        mapper: ForecastResponseMapper,
        dispatcherProvider: DispatcherProvider,
        json: Json
    ): WeatherForecastRepository =
        WeatherForecastRepositoryImpl(
            api = api,
            apiConfig = apiConfig,
            mapper = mapper,
            dispatcherProvider = dispatcherProvider,
            json = json
        )
}