package com.example.campuswheels.core.domain.models

data class Location(
    val _id: String,
    val lat: Double,
    val lng: Double,
    val address: String?=null
)