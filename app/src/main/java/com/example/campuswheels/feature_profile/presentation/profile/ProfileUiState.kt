package com.example.campuswheels.feature_profile.presentation.profile

import com.example.campuswheels.core.domain.models.User

data class ProfileUiState(
    val token : String= "",
    val user : User?=null,
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val error : String?=null
)
