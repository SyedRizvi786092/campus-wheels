package com.example.campuswheels.core.domain.repository

import android.location.Location
import com.example.campuswheels.core.domain.models.BusLocation
import com.example.campuswheels.core.domain.models.UniversityBus
import kotlinx.coroutines.flow.Flow


interface LocationRepository {
    fun getLocation(
//        fusedLocationClient : FusedLocationProviderClient,
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit
    )

//    fun getCurrentLocation(
//        callback: (Location) -> Unit,
//        onError: (Exception) -> Unit,
//        isLive: Boolean,
//        updateInterval: Long = 1000
//    )

    suspend fun shareLocation(
        userId: String,
        latitude: Double,
        longitude: Double,
        timestamp: Long = System.currentTimeMillis()
    )

    suspend fun getAllBusLocations(): List<BusLocation>

    suspend fun simulateBusMovement(
        bus: UniversityBus,
        onSuccess: (UniversityBus) -> Unit
    )

    suspend fun observeBusLocations(onUpdate: (List<BusLocation>) -> Unit)

    suspend fun getUniversityBuses(): Flow<List<UniversityBus>>
}
