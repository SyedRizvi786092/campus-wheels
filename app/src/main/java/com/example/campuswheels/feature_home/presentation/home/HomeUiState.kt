package com.example.campuswheels.feature_home.presentation.home

import android.location.Location
import com.example.campuswheels.core.domain.models.UniversityBus
import com.example.campuswheels.feature_bus.domain.models.BusWithRoute
import com.example.campuswheels.feature_bus_stop.domain.model.BusStopWithRoutes

data class HomeUiState(
    val nearbyBusStops : List<BusStopWithRoutes> = emptyList(),
    val allBuses : List<UniversityBus> = emptyList(),
    val location : Location?=null,
    val isLoadingNearbyStops : Boolean = false,
    val isLoadingNearbyBuses : Boolean = false,
    val isLoadingLocation : Boolean = false,
    val isRefreshingNearbyStops : Boolean = false,
    val isRefreshingNearbyBuses : Boolean = false,
    val errorNearbyBuses : String?=null,
    val errorNearbyStops : String?=null,
    val errorLocation : String?=null
)
