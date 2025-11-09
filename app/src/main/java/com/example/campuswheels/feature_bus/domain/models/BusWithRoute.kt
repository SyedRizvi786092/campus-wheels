package com.example.campuswheels.feature_bus.domain.models

import com.example.campuswheels.core.domain.models.BusInfo
import com.example.campuswheels.core.domain.models.BusRoute
import com.example.campuswheels.core.domain.models.Location

data class BusWithRoute(
    val _id: String,
    val vehNo: String,
    val status: String?,
    val info: BusInfo,
    val location: Location?,
    val route: BusRoute,
    val createdAt: String,
    val updatedAt: String,
)