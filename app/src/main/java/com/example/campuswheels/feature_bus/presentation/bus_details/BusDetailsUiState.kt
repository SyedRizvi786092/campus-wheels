package com.example.campuswheels.feature_bus.presentation.bus_details

import com.example.campuswheels.core.domain.models.Bus
import com.example.campuswheels.feature_bus_routes.domain.models.BusRouteWithStops

data class BusDetailsUiState(
    val route : BusRouteWithStops?=null,
    val bus : Bus?=null,
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val error : String?=null
)