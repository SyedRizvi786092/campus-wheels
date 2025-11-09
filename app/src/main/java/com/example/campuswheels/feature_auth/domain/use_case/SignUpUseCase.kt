//package com.example.campuswheels.feature_auth.domain.use_case
//
//import com.example.campuswheels.core.domain.repository.UserPrefsRepository
//import com.example.campuswheels.core.util.ApiHandler
//import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_auth.data.remote.dto.request.SignUpUserRequest
//import com.example.campuswheels.feature_auth.data.remote.dto.response.AuthResponse
//import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class SignUpUseCase @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val userPrefsRepository: UserPrefsRepository
//) : ApiHandler() {
//    operator fun invoke(
//        name: String,
//        email: String,
//        phone: String,
//        password: String,
//    ): Flow<Resource<AuthResponse>> = makeRequest(
//        apiCall = {
//            authRepository.signUpUser(
//                SignUpUserRequest(
//                    name,
//                    phone,
//                    email,
//                    password
//                )
//            )
//        },
//        onSuccess = {result ->
//            val token = result.token
//            val userType = result.user.userType
//            userPrefsRepository.updateUserType(userType)
//            userPrefsRepository.updateToken(token)
//        }
//    )
//}

package com.example.campuswheels.feature_auth.domain.use_case

import com.example.campuswheels.core.domain.repository.UserPrefsRepository
import com.example.campuswheels.core.util.ApiHandler
import com.example.campuswheels.core.util.Resource
import com.example.campuswheels.feature_auth.data.remote.dto.request.SignUpUserRequest
import com.example.campuswheels.feature_auth.data.remote.dto.response.AuthResponse
import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPrefsRepository: UserPrefsRepository
) : ApiHandler() {

    operator fun invoke(
        name: String,
        email: String,
        phone: String,
        password: String,
    ): Flow<Resource<AuthResponse>> = makeRequest(
        apiCall = {
            authRepository.signUpUser(
                SignUpUserRequest(name, phone, email, password)
            )
        },
        onSuccess = { result ->
            userPrefsRepository.updateEmail(result.email)
            userPrefsRepository.updateUserId(result.userId)
            userPrefsRepository.updateIsEmailVerified(result.isEmailVerified)
        }
    )
}
