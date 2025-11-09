package com.example.campuswheels.feature_bus_stop.presentation.stop_details

import com.example.campuswheels.feature_bus.domain.models.BusWithRoute
import com.example.campuswheels.feature_bus_stop.domain.model.BusStopWithRoutes

data class StopDetailsUiState(
    val stop : BusStopWithRoutes?=null,
    val buses : List<BusWithRoute> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val error : String?=null
)
