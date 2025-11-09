package com.example.campuswheels.core.domain.models

// Mine
data class UniversityBus(
    val id: String,
    val vehicleNumber: String,
    val route: Route,
    val currentLocation: BusLocation? = null // For real-time tracking
)
