//package com.example.campuswheels.feature_bus.domain.use_cases
//
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_bus.domain.models.BusWithRoute
//import com.example.campuswheels.feature_bus.domain.repository.BusRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class GetBusesForStopUseCase @Inject constructor(
//    private val busRepository: BusRepository
//) : ApiHandler(){
//    operator fun invoke(stopNo : String) : Flow<Resource<List<BusWithRoute>>> = makeRequest(
//        apiCall = {
//            busRepository.getBusesForStop(stopNo)
//        }
//    )
//}