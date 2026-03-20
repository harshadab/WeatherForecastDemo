package com.assignment.core.uicomponents.views

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.assignment.core.location_helper.LocationHelper

@Composable
fun LocationPermission(
    locationHelper: LocationHelper,
    onCityReceived: (String) -> Unit
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentCity(locationHelper, onCityReceived)
            } else {
                onCityReceived(LocationHelper.DEFAULT_CITY)
            }
        }

    LaunchedEffect(Unit) {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            getCurrentCity(locationHelper, onCityReceived)
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun getCurrentCity(
    locationHelper: LocationHelper,
    onCityReceived: (String) -> Unit
) {
    locationHelper.getCurrentCity { city ->
        onCityReceived(city)
    }
}
