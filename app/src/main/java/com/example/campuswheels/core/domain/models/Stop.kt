package com.example.campuswheels.core.domain.models

import com.google.android.gms.maps.model.LatLng

// Mine
data class Stop(
    val stopNumber: Int,
    val name: String,
    val coordinates: LatLng,
    val arrivalTime: String // e.g. "07:14"
)
