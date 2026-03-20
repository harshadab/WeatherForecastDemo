package com.assignment.core.location_helper

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.Locale
import javax.inject.Inject

class LocationHelper @Inject constructor(
    @param:ActivityContext
    private val context: Context
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentCity(onResult: (String) -> Unit) {
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                location?.let {
                    getCityName(it.latitude, it.longitude) { city ->
                        if (city != null) {
                            onResult(city)
                        } else {
                            onResult(DEFAULT_CITY)
                        }
                    }
                } ?: onResult(DEFAULT_CITY)
            }
            .addOnFailureListener { onResult(DEFAULT_CITY) }
    }

    private fun getCityName(latitude: Double, longitude: Double, onResult: (String?) -> Unit) {
        val geocoder = Geocoder(
            context,
            Locale.getDefault()
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude,
                longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        val address = addresses[0]
                        val city = address.locality
                            ?: address.subAdminArea
                            ?: address.adminArea
                        onResult(city)
                    }

                    override fun onError(errorMessage: String?) {
                        onResult(null)
                    }
                })
        } else {
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val city = addresses?.firstOrNull()?.locality
                onResult(city)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    companion object {
        const val DEFAULT_CITY = "Stockholm"
    }
}
