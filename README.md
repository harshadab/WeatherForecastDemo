# WeatherForecastDemo

A small weather forecast app built with **Kotlin + Jetpack Compose**.
It shows **current conditions**, an **hourly forecast**, and a **7‑day forecast** for your **current location** or any **searched city**.

## Features
- Current location weather (location permission + geocoding)
- Search weather by city name
- Current conditions + hourly forecast
- 7‑day forecast
- Pull to refresh

## Tech stack
- Kotlin, Coroutines/Flow
- Jetpack Compose (Material 3)
- Hilt (DI)
- Retrofit + Kotlinx Serialization
- Coil (weather icons)
- MVVM + Clean Architecture

## Modules
- `:app` – application entry point
- `:core` – shared utilities, UI components, location helper, dispatcher provider
- `:feature:weather` – weather feature (data/domain/presentation)

## Setup
1. Get an API key from WeatherAPI:
    - `https://www.weatherapi.com/`

2. Add the key as a Gradle property named `WEATHER_API_KEY`.

   Recommended: add it to **`~/.gradle/gradle.properties`** (so it is not committed):
   ```properties
   WEATHER_API_KEY=YOUR_KEY_HERE