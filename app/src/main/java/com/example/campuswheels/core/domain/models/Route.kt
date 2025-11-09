package com.example.campuswheels.core.domain.models

// Mine
data class Route(
    val routeNumber: String,
    val name: String,
    val stops: List<Stop>
)
