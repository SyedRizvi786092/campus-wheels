//package com.example.campuswheels.feature_bus_stop.domain.use_case
//
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_bus_stop.domain.model.BusStopWithRoutes
//import com.example.campuswheels.feature_bus_stop.domain.repository.BusStopRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class GetAllBusStopsUseCase @Inject constructor(
//    private val busStopRepository: BusStopRepository
//) : ApiHandler(){
//    operator fun invoke() : Flow<Resource<List<BusStopWithRoutes>>> = makeRequest(
//        apiCall = {
//            busStopRepository.getAllBusStops()
//        }
//    )
//}
