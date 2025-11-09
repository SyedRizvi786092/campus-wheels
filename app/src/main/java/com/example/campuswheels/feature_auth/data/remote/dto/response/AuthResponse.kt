package com.example.campuswheels.feature_auth.data.remote.dto.response

data class AuthResponse(
    val userId : String,
    val email : String,
    val isEmailVerified: Boolean
)