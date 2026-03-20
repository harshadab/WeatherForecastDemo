package com.assignment.weatherforecastdemo.di

import com.assignment.weather.data.api.ApiConfig
import com.assignment.weatherforecastdemo.api_config.ApiConfigImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiConfigModule {

    @Provides
    @Singleton
    fun provideApiConfig(): ApiConfig = ApiConfigImpl()

}
