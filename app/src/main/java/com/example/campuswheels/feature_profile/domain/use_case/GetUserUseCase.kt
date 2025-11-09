////package com.example.campuswheels.feature_profile.domain.use_case
////
////import com.example.campuswheels.core.data.remote.dto.ApiResponse
////import com.example.campuswheels.core.util.ApiHandler
////import com.example.campuswheels.core.util.Resource
////import com.example.campuswheels.feature_profile.data.remote.dto.toUser
////import com.example.campuswheels.core.domain.models.User
////import com.example.campuswheels.feature_profile.domain.repository.UserRepository
////import kotlinx.coroutines.flow.Flow
////import javax.inject.Inject
////
////class GetUserUseCase @Inject constructor(
////    private val userRepository: UserRepository
////) : ApiHandler() {
////    operator fun invoke() : Flow<Resource<User>> = makeRequest(
////        apiCall = {
////            val apiResponse = userRepository.getUser()
////            ApiResponse(
////                success = apiResponse.success,
////                data = apiResponse.data.toUser(),
////                message = apiResponse.message
////            )
////        }
////    )
////}
//
//package com.example.campuswheels.feature_profile.domain.use_case
//
//import com.example.campuswheels.core.data.remote.dto.ApiResponse
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_profile.data.remote.dto.toUser
//import com.example.campuswheels.core.domain.models.User
////import com.example.campuswheels.feature_profile.domain.repository.UserRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class GetUserUseCase @Inject constructor(
////    private val userRepository: UserRepository
//) : ApiHandler() {
//
//    operator fun invoke(): Flow<Resource<User>> = makeRequest(
//        apiCall = {
//            when (val response = userRepository.getUser()) {
//                is ApiResponse.Success -> ApiResponse.Success(response.data.toUser())
//                is ApiResponse.Error -> ApiResponse.Error(response.message)
//            }
//        }
//    )
//}
