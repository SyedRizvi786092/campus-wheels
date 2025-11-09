//package com.example.campuswheels.feature_bus_routes.domain.use_case
//
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_bus_routes.domain.models.BusRouteWithStops
//import com.example.campuswheels.feature_bus_routes.domain.repository.BusRouteRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//
//class GetBusRouteUseCase @Inject constructor(
//    private val busRouteRepository: BusRouteRepository
//) : ApiHandler(){
//    operator fun invoke(routeNo : String) : Flow<Resource<BusRouteWithStops>> = makeRequest(
//        apiCall = {
//            busRouteRepository.getBusRoute(routeNo)
//        }
//    )
//}