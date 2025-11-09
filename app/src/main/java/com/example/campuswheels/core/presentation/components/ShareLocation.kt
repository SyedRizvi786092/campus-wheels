package com.example.campuswheels.core.presentation.components

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.campuswheels.feature_home.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun LocationPermissionScreen() {
//    val logger  = LoggerUtil("LocationPermissionScreen")
//
//    val context: Context = LocalContext.current
//
//    val settingResultRequest = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult()
//    ) { activityResult ->
//        if (activityResult.resultCode == 200)
//            logger.info("Accepted : ${activityResult.resultCode}")
//        else {
//            logger.info("Denied : ${activityResult.resultCode}")
//        }
//    }
//
//    Column() {
//        Spacer(modifier = Modifier.height(40.dp))
//        Button(onClick = {
//            checkLocationSetting(
//                context = context,
//                onDisabled = { intentSenderRequest ->
//                    settingResultRequest.launch(intentSenderRequest)
//                },
//                onEnabled = { /* This will call when setting is already enabled */ }
//            )
//        }) {
//            Text(text = "Request permission")
//        }
//        Spacer(modifier = Modifier.height(20.dp))
////        LocationFetcher(onLocationFetched = { lat,lng->
////
////        })
//    }
//}

@Composable
fun ShareLocation(viewModel: HomeViewModel) {
    val context = LocalContext.current
//    val fusedLocationClient by remember{ mutableStateOf(LocationServices.getFusedLocationProviderClient(context)) }
//    var locationState by remember{mutableStateOf<Location?>(null)}
//    val logger  = LoggerUtil("LocationFetcher")
//
//    LaunchedEffect(Unit) {
//        try {
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                logger.error("Permission not granted", "LocationFetcher")
//            } else {
//                fusedLocationClient.lastLocation
//                    .addOnSuccessListener { location ->
//                        logger.info("Location : $location", "LocationFetcher")
//
//                        if(location != null) {
//                            locationState = location
//                        }
//                        // Got last known location. In some rare situations this can be null.
//                    }
//            }
//        } catch (e: Exception) {
//            logger.error("$e", "LocationFetcher")
//            // Handle location fetching error
//        }
//    }

    Button(onClick = {
        viewModel.postDeviceLocation()
    }) {
        Text(text = "Share Location")
    }
}

fun sendLocationToWebhook(lat: Double, lng: Double) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val url = URL("https://webhook.site/983e98d4-f829-45fb-8050-efd6c395fa2c")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val jsonBody = """
                {
                    "latitude": $lat,
                    "longitude": $lng,
                    "timestamp": "$timestamp"
                }
            """.trimIndent()

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(jsonBody)
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            Log.d("WBHK", "sendLocationToWebhook: Webhook response: $responseCode")
//            println("Webhook response: $responseCode")
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

//// call this function on button click  // Enable GPS
//private fun checkLocationSetting(
//    context: Context,
//    onDisabled: (IntentSenderRequest) -> Unit,
//    onEnabled: () -> Unit
//) {
//    val locationRequest = LocationRequest.Builder(1000)
////        .setIntervalMillis(1000)
//        .setMinUpdateIntervalMillis(1000)
//        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        .build()
////    val locationRequest = LocationRequest.create().apply {
////        interval = 1000
////        fastestInterval = 1000
////        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
////    }
//
//    val client: SettingsClient = LocationServices.getSettingsClient(context)
//    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
//        .Builder()
//        .addLocationRequest(locationRequest)
//
//    val gpsSettingTask: Task<LocationSettingsResponse> =
//        client.checkLocationSettings(builder.build())
//
//    gpsSettingTask.addOnSuccessListener { onEnabled() }
//    gpsSettingTask.addOnFailureListener { exception ->
//        if (exception is ResolvableApiException) {
//            try {
//                val intentSenderRequest = IntentSenderRequest
//                    .Builder(exception.resolution)
//                    .build()
//                onDisabled(intentSenderRequest)
//            } catch (sendEx: IntentSender.SendIntentException) {
//                // ignore here
//            }
//        }
//    }
//}

//@Composable
//fun GpsLayout() {
//    val logger  = LoggerUtil("LocationPermissionScreen")
//
//    val context: Context = LocalContext.current
//
//    val settingResultRequest = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult()
//    ) { activityResult ->
//        if (activityResult.resultCode == 200)
//            logger.info("Accepted : ${activityResult.resultCode}")
//        else {
//            logger.info("Denied : ${activityResult.resultCode}")
//        }
//    }
//
//    Button(onClick = {
//        checkLocationSetting(
//            context = context,
//            onDisabled = { intentSenderRequest ->
//                settingResultRequest.launch(intentSenderRequest)
//            },
//            onEnabled = { /* This will call when setting is already enabled */ }
//        )
//    }) {
//        Text(text = "Request permission")
//    }
//
//}

//private fun turnOnLocation(
//    context: Context,
//    onDisabled: (IntentSenderRequest) -> Unit,
//    onEnabled: () -> Unit
//) {
//    val settingsClient = LocationServices.getSettingsClient(context)
//        val locationRequest = LocationRequest.create().apply {
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//    LocationRequest.Builder(122).set
////    val locationRequest = LocationRequest.create().apply {
////        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
////    }
//    val builder = LocationSettingsRequest.Builder()
//        .addLocationRequest(locationRequest)
//    val task = settingsClient.checkLocationSettings(builder.build())
//
//    task.addOnSuccessListener {
//        // Location settings are already satisfied
//        Log.d("Location", "Location settings are already satisfied")
//    }
//
//    task.addOnFailureListener { exception ->
//        if (exception is ResolvableApiException) {
//            try {
//                // Location settings are not satisfied, but can be resolved by showing the user a dialog.
////                exception.startResolutionForResult(context, REQUEST_LOCATION_SETTINGS)
//
//                val intentSenderRequest = IntentSenderRequest
//                    .Builder(exception.resolution)
//                    .build()
//                onDisabled(intentSenderRequest)
//
//            } catch (sendEx: IntentSender.SendIntentException) {
//                // Error handling
//                Log.e("Location", "Error resolving location settings: ${sendEx.message}")
//            }
//        } else {
//            // Location settings are not satisfied and cannot be resolved.
//            Log.e("Location", "Location settings are not satisfied.")
//        }
//    }
//}
