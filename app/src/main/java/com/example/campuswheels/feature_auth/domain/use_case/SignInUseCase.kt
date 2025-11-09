//package com.example.campuswheels.feature_auth.domain.use_case
//
//import com.example.campuswheels.core.domain.repository.UserPrefsRepository
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_auth.data.remote.dto.request.SignInUserRequest
//import com.example.campuswheels.feature_auth.data.remote.dto.response.AuthResponse
//import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class SignInUseCase @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val userPrefsRepository : UserPrefsRepository
//) : ApiHandler(){
//    operator fun invoke(
//        email : String,
//        password : String,
//    ) : Flow<Resource<AuthResponse>> = makeRequest(
//        apiCall = {
//            authRepository.signInUser(
//                SignInUserRequest(email,password)
//            )
//        },
//        onSuccess = {result ->
//            val token = result.token
//            val userType = result.user.userType
//            userPrefsRepository.updateUserType(userType)
//            userPrefsRepository.updateToken(token)
//        }
//    )
//
//}

package com.example.campuswheels.feature_auth.domain.use_case

import com.example.campuswheels.core.domain.repository.UserPrefsRepository
import com.example.campuswheels.core.util.ApiHandler
import com.example.campuswheels.core.util.Resource
import com.example.campuswheels.feature_auth.data.remote.dto.request.SignInUserRequest
import com.example.campuswheels.feature_auth.data.remote.dto.response.AuthResponse
import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPrefsRepository: UserPrefsRepository
) : ApiHandler() {

    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Resource<AuthResponse>> = makeRequest(
        apiCall = {
            authRepository.signInUser(SignInUserRequest(email, password))
        },
        onSuccess = { result ->
            // If you still want to store info, use Firebase fields like email or userId
            userPrefsRepository.updateEmail(result.email)
            userPrefsRepository.updateUserId(result.userId)
        }
    )
}
