package com.example.campuswheels.core.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.campuswheels.core.domain.models.BusLocation
import com.example.campuswheels.core.domain.models.Route
import com.example.campuswheels.core.domain.models.Stop
import com.example.campuswheels.core.domain.models.UniversityBus
import com.example.campuswheels.core.domain.repository.LocationRepository
import com.example.campuswheels.core.util.LocationUtil.getSmallRandomDelta
import com.example.campuswheels.core.util.LoggerUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val firestore: FirebaseFirestore
) : LocationRepository {
    private val logger = LoggerUtil("LocationRepositoryImpl")

    override fun getLocation(
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            if (
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw Exception("Fine location permission not granted!")
            } else {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        logger.info("Location : $location", "getLocation")

                        if (location != null) {
                            onSuccess(location) // Got last known location. In some rare situations this can be null.
                        } else {
                            onError(Exception("Couldn't fetch device's location!"))
                        }
                    }
                    .addOnFailureListener {
                        onError(it)
                    }
            }
        } catch (e: Exception) {
            logger.error("$e", "getLocation")
            onError(e)
        }
    }

//    override fun getCurrentLocation(
//        callback: (Location) -> Unit,
//        onError : (Exception)->Unit,
//        isLive : Boolean,
//        updateInterval : Long
//    ) {
//        try {
//            if (
//                ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                throw Exception("Fine location permission not granted!")
//            } else {
//                val locationRequest = LocationRequest.create().apply {
//                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                    interval = 1000 // Update interval in milliseconds
//                    fastestInterval = updateInterval // Fastest update interval in milliseconds, emit updated location
//                }
//                val locationCallback = object : LocationCallback() {
//                    override fun onLocationResult(locationResult: LocationResult) {
//                        val lastLocation = locationResult.lastLocation
//                        logger.info("Location : $lastLocation", "getCurrentLocation")
//                        if (lastLocation != null) {
//                            if(!isLive){
//                                // remove listener
//                                fusedLocationClient.removeLocationUpdates(this)
//                            }
//                            callback(lastLocation)
//                        }
//                    }
//                }
//                fusedLocationClient.requestLocationUpdates(
//                    locationRequest,locationCallback,null
//                )
//
//            }
//        } catch (e: Exception) {
//            logger.error("$e", "getLocation")
//            onError(e)
//        }
//    }

    override suspend fun shareLocation(
        userId: String,
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ) {
        val data = hashMapOf(
            "userId" to userId,
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to timestamp
        )

        firestore.collection("device_locations")
            .document(userId) // overwrite user's location
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Location shared successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                logger.error(e, "Unable to post location on Firestore!")
                Toast.makeText(context, "Error occurred while sharing location!", Toast.LENGTH_SHORT).show()
            }
    }

    override suspend fun getAllBusLocations(): List<BusLocation> {
        val snapshot = firestore.collection("bus_locations").get().await()

        return snapshot.documents.mapNotNull { doc ->
            val lat = doc.getDouble("latitude")
            val lng = doc.getDouble("longitude")
            val timestamp = doc.getLong("timestamp")
            if (lat != null && lng != null && timestamp != null) {
                BusLocation(
                    latitude = lat,
                    longitude = lng,
                    timestamp = timestamp
                )
            } else null
        }
    }

    override suspend fun simulateBusMovement(
        bus: UniversityBus,
        onSuccess: (UniversityBus) -> Unit
    ) {
        val updatedLat = bus.currentLocation?.latitude?.plus(getSmallRandomDelta())
        val updatedLng = bus.currentLocation?.longitude?.plus(getSmallRandomDelta())
        val updatedBus = bus.copy(
            currentLocation = BusLocation(
                latitude = updatedLat,
                longitude = updatedLng,
                timestamp = System.currentTimeMillis()
            )
        )

        if (updatedLat != null && updatedLng != null) {
            firestore.collection("buses")
                .document(bus.id)
                .collection("currentLocation")
                .document("location") // fixed doc ID for consistent overwrite
                .set(
                    mapOf(
                        "coordinates" to GeoPoint(updatedLat, updatedLng),
                        "timestamp" to System.currentTimeMillis()
                    )
                )
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "Updated location for ${bus.id}")
                    onSuccess(updatedBus)
                }
                .addOnFailureListener { exc ->
                    Log.e("FirestoreUpdate", "Failed to update location for ${bus.id}", exc)
                }
        }
    }

    override suspend fun observeBusLocations(onUpdate: (List<BusLocation>) -> Unit) {
        firestore.collection("bus_locations")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val updatedLocations = snapshot.documents.mapNotNull { doc ->
                    val lat = doc.getDouble("latitude")
                    val lng = doc.getDouble("longitude")
                    val timestamp = doc.getLong("timestamp")
                    if (lat != null && lng != null && timestamp != null) {
                        BusLocation(lat, lng, timestamp)
                    } else null
                }
                onUpdate(updatedLocations)
            }
    }

    override suspend fun getUniversityBuses(): Flow<List<UniversityBus>> = callbackFlow {
        val busesCollection = firestore.collection("buses")
        val listeners = mutableListOf<ListenerRegistration>()

        // Step 1: Get static data first
        busesCollection.get()
            .addOnSuccessListener { snapshot ->
                val busList = mutableListOf<UniversityBus>()

                snapshot.documents.forEach { busDoc ->
                    val busId = busDoc.id
                    val vehicleNumber = busDoc.getString("vehicleNumber") ?: return@forEach
                    val routeRef = busDoc.getDocumentReference("route") ?: return@forEach

                    // Fetch route details
                    routeRef.get().addOnSuccessListener { routeSnap ->
                        val routeNumber = routeSnap.getString("routeNumber") ?: ""
                        val routeName = routeSnap.getString("routeName") ?: ""

                        routeSnap.reference.collection("stops")
                            .orderBy("stopNumber")
                            .get()
                            .addOnSuccessListener { stopsSnap ->
                                val stops = stopsSnap.documents.mapNotNull { stopDoc ->
                                    val stopNumber = stopDoc.getLong("stopNumber")?.toInt() ?: return@mapNotNull null
                                    val name = stopDoc.getString("name") ?: return@mapNotNull null
                                    val arrivalTime = stopDoc.getString("arrivalTime") ?: ""
                                    val coordinates = stopDoc.getGeoPoint("coordinates")?.let {
                                        LatLng(it.latitude, it.longitude)
                                    } ?: return@mapNotNull null

                                    Stop(stopNumber, name, coordinates, arrivalTime)
                                }

                                val route = Route(routeNumber, routeName, stops)

                                // Set up a snapshot listener for live location updates
                                val locDocRef = busDoc.reference.collection("currentLocation").document("location")
                                val listener = locDocRef.addSnapshotListener { locSnap, _ ->
                                    val busLocation = locSnap?.let {
                                        val geoPoint = it.getGeoPoint("coordinates")
                                        val timestamp = it.getLong("timestamp") ?: 0L
                                        if (geoPoint != null) {
                                            BusLocation(geoPoint.latitude, geoPoint.longitude, timestamp)
                                        } else null
                                    }

                                    val existingIndex = busList.indexOfFirst { it.id == busId }
                                    val updatedBus = UniversityBus(busId, vehicleNumber, route, busLocation)

                                    if (existingIndex >= 0) {
                                        busList[existingIndex] = updatedBus
                                    } else {
                                        busList.add(updatedBus)
                                    }

                                    trySend(busList.toList()) // Emit updated list
                                }

                                listeners.add(listener)
                            }
                    }
                }
            }
            .addOnFailureListener {
                close(it) // If failed to get buses
            }

        awaitClose {
            // Remove all listeners when flow collection is cancelled
            listeners.forEach { it.remove() }
        }
    }
}
