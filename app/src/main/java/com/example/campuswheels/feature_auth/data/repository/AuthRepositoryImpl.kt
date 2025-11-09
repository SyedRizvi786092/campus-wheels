package com.example.campuswheels.feature_auth.data.repository

import com.example.campuswheels.core.data.remote.dto.ApiResponse
import com.example.campuswheels.feature_auth.data.remote.dto.response.AuthResponse
import com.example.campuswheels.feature_auth.data.remote.dto.request.SignInUserRequest
import com.example.campuswheels.feature_auth.data.remote.dto.request.SignUpUserRequest
import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

//class AuthRepositoryImpl(
//    private val authApiService: AuthApiService,
//    private val defaultDispatcher: CoroutineDispatcher
//) : AuthRepository{
//
//    override suspend fun signInUser(requestBody: SignInUserRequest): ApiResponse<AuthResponse> =  withContext(defaultDispatcher){
//        authApiService.signInUser(requestBody)
//    }
//
//    override suspend fun signUpUser(requestBody: SignUpUserRequest): ApiResponse<AuthResponse> = withContext(defaultDispatcher){
//        authApiService.signUpUser(requestBody)
//    }
//}

class AuthRepositoryImpl @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInUser(requestBody: SignInUserRequest): ApiResponse<AuthResponse> = withContext(defaultDispatcher) {
        try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(requestBody.email, requestBody.password)
                .await()

            val user = result.user
            if (user != null) {
                ApiResponse.Success(
                    AuthResponse(
                        userId = user.uid,
                        email = user.email.orEmpty(),
                        isEmailVerified = user.isEmailVerified
                    )
                )
            } else {
                ApiResponse.Error("User is null")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Sign-in failed")
        }
    }

    override suspend fun signUpUser(requestBody: SignUpUserRequest): ApiResponse<AuthResponse> = withContext(defaultDispatcher) {
        try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(requestBody.email, requestBody.password)
                .await()

            val user = result.user
            if (user != null) {
                ApiResponse.Success(
                    AuthResponse(
                        userId = user.uid,
                        email = user.email.orEmpty(),
                        isEmailVerified = user.isEmailVerified
                    )
                )
            } else {
                ApiResponse.Error("User is null")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Sign-up failed")
        }
    }
}
