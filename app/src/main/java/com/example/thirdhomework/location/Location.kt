package com.example.thirdhomework.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.thirdhomework.data.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Location(private val context: Context) {
    private lateinit var client: FusedLocationProviderClient
    private var mLastLocation: Location? = null
    private var coordinate: Coordinate? = null


    fun getCoordinates() {
        client = LocationServices.getFusedLocationProviderClient(context)
        if (isPermitted()) {
            client.lastLocation.addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result
                    }
                }
            }
        }
    }

    private fun isPermitted() =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    companion object {
        const val REQUEST_CODE = 100
    }
}