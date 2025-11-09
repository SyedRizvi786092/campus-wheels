package com.example.campuswheels.feature_bus_routes.domain.repository

import com.example.campuswheels.core.domain.models.Route

//import com.example.campuswheels.core.data.remote.dto.ApiResponse
//import com.example.campuswheels.feature_bus_routes.domain.models.BusRouteWithStops

interface BusRouteRepository {
//    suspend fun getAllBusRoutes() : ApiResponse<List<BusRouteWithStops>>
//    suspend fun getBusRoute(routeNo : String) : ApiResponse<BusRouteWithStops>

    suspend fun fetchAllBusRoutes(
        onSuccess: (List<Route>) -> Unit,
        onFailure: (Exception) -> Unit
    )
}
