package com.assignment.weather.data.repository

import com.assignment.core.providers.DispatcherProvider
import com.assignment.core.Result
import com.assignment.core.utils.orDefault
import com.assignment.weather.data.api.ApiConfig
import com.assignment.weather.data.api.WeatherApiService
import com.assignment.weather.data.mapper.ForecastResponseMapper
import com.assignment.weather.data.models.ErrorResponse
import com.assignment.weather.data.models.ForecastResponseDto
import com.assignment.weather.domain.models.ForecastLocation
import com.assignment.weather.domain.repository.WeatherForecastRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

internal class WeatherForecastRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val apiConfig: ApiConfig,
    private val mapper: ForecastResponseMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val json: Json
): WeatherForecastRepository {

    override suspend fun getWeatherForecast(city: String, days: Int): Result<ForecastLocation> =
        withContext(dispatcherProvider.ioDispatcher) {
            try {
                val response = api.getForecast(apiConfig.apiKey, city, days)
                if (response.isSuccessful) {
                    mapSuccess(response)
                } else {
                    mapError(response)
                }
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: IOException) {
                Result.Error(message = NETWORK_ERROR, code = null)
            } catch (ex: SerializationException) {
                Result.Error(message = PARSING_ERROR, code = null)
            } catch (ex: Exception) {
                Result.Error(message = ex.localizedMessage.orDefault(UNKNOWN_ERROR), code = null)
            }
        }

    private fun mapSuccess(response: Response<ForecastResponseDto>): Result<ForecastLocation> =
        response.body()?.let {
            Result.Success(mapper.map(it))
        } ?: Result.Error(
            message = EMPTY_RESPONSE_BODY,
            code = response.code()
        )

    private fun mapError(response: Response<ForecastResponseDto>): Result<ForecastLocation> {
        val errorBody = response.errorBody()?.string()
        val errorResponse: ErrorResponse? = errorBody?.let {
            runCatching<ErrorResponse> {
                json.decodeFromString(
                    ErrorResponse.serializer(),
                    it
                )
            }.getOrNull()
        }
        return Result.Error(
            message = errorResponse?.error?.message.orDefault(UNKNOWN_ERROR),
            code = errorResponse?.error?.code
        )
    }

    companion object {
        private const val EMPTY_RESPONSE_BODY = "Empty response body"
        private const val UNKNOWN_ERROR = "Unknown error"
        private const val NETWORK_ERROR = "Network error"
        private const val PARSING_ERROR = "Parsing error"
    }
}
