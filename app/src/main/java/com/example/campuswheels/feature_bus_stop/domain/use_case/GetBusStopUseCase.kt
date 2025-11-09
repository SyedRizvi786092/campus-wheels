//package com.example.campuswheels.feature_bus_stop.domain.use_case
//
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_bus_stop.domain.model.BusStopWithRoutes
//import com.example.campuswheels.feature_bus_stop.domain.repository.BusStopRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class GetBusStopUseCase @Inject constructor(
//    private val busStopRepository: BusStopRepository
//) : ApiHandler(){
//    operator fun invoke(stopNo : String) : Flow<Resource<BusStopWithRoutes>> = makeRequest(
//        apiCall = {
//            busStopRepository.getBusStop(stopNo)
//        }
//    )
//}