package com.assignment.weather.di

import com.assignment.weather.domain.usecases.GetWeatherForecastUC
import com.assignment.weather.domain.usecases.GetWeatherForecastUCImpl
import com.assignment.weather.domain.repository.WeatherForecastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal object GetWeatherForecastUCModule {

    @Provides
    fun provideGetForecastUseCase(repository: WeatherForecastRepository): GetWeatherForecastUC {
        return GetWeatherForecastUCImpl(repository)
    }

}
