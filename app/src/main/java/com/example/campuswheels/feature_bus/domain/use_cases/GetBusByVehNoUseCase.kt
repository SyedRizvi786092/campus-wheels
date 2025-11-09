//package com.example.campuswheels.feature_bus.domain.use_cases
//
//import com.example.campuswheels.core.domain.models.Bus
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_bus.domain.repository.BusRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class GetBusByVehNoUseCase @Inject constructor(
//    private val busRepository: BusRepository
//) : ApiHandler(){
//    operator fun invoke(vehNo : String) : Flow<Resource<Bus>> = makeRequest(
//        apiCall = {
//            busRepository.getBusByVehNo(vehNo)
//        }
//    )
//}