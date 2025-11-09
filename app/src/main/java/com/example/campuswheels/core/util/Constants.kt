package com.example.campuswheels.core.util

import com.example.campuswheels.R

object Constants {
//    private const val baseUrl = "http://192.168.223.98:3000"   // New2
//    const val baseUrl = "http://192.168.244.98:3000"   // New
//    private const val baseUrl = "http://192.168.43.98:3000" //Old
//    const val apiBaseUrl = "$baseUrl/api/"
//    const val socketBaseUrl = baseUrl

    const val userPrefs = "user_prefs"

    val busColor = mapOf<String, Int>(
        "red" to R.color.red,
        "green" to R.color.green,
        "blue" to R.color.blue,
        "orange" to R.color.orange,
        "other" to R.color.black
    )
    val busIcon = mapOf<String, Int>(
        "red" to R.drawable.bus_location_red,
        "green" to R.drawable.bus_location_green,
        "blue" to R.drawable.bus_location_blue,
        "orange" to R.drawable.bus_location_orange,
        "other" to R.drawable.locate_bus
    )

    val busStatus = mapOf<String, String>(
        "na" to "NA",
        "in_route" to "IN ROUTE",
        "delayed" to "DELAYED",
        "cancelled" to "CANCELLED",
        "break_down" to "BREAK DOWN",
    )

    object UserType {
        const val USER = "user"
        const val driverCoordinator = "driverCoordinator"
    }

    val days = listOf("sunday", "monday", "tuesday", "wednesday", "thursday","friday","saturday")
}
