package com.example.campuswheels.feature_bus_routes.data.repository

//import com.example.campuswheels.core.data.remote.dto.ApiResponse
//import com.example.campuswheels.feature_bus_routes.data.remote.api.BusRouteApiService
//import com.example.campuswheels.feature_bus_routes.domain.models.BusRouteWithStops
import android.util.Log
import com.example.campuswheels.core.domain.models.Route
import com.example.campuswheels.core.domain.models.Stop
import com.example.campuswheels.feature_bus_routes.domain.repository.BusRouteRepository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.withContext

class BusRouteRepositoryImpl(
    private val firestore: FirebaseFirestore
//    private val routeApiService : BusRouteApiService,
//    private val defaultDispatcher : CoroutineDispatcher
) : BusRouteRepository {

//    override suspend fun getAllBusRoutes(): ApiResponse<List<BusRouteWithStops>>  = withContext(defaultDispatcher){
//        routeApiService.getAllBusRoutes()
//    }

    //    override suspend fun getBusRoute(routeNo: String): ApiResponse<BusRouteWithStops> = withContext(defaultDispatcher){
//        routeApiService.getBusRoute(routeNo)
//    }

    override suspend fun fetchAllBusRoutes(
        onSuccess: (List<Route>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("bus_routes")
            .get()
            .addOnSuccessListener { routeDocuments ->
                val routes = mutableListOf<Route>()
                val totalRoutes = routeDocuments.size()
                var processedRoutes = 0

                if (totalRoutes == 0) {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                }

                routeDocuments.forEach { routeDoc ->
                    val routeNumber = routeDoc.getString("routeNumber") ?: ""
                    val routeName = routeDoc.getString("routeName") ?: ""
                    val routeRef = routeDoc.reference

                    routeRef.collection("stops").orderBy("stopNumber")
                        .get()
                        .addOnSuccessListener { stopDocs ->
                            val stops = stopDocs.map { stopDoc ->
                                Stop(
                                    stopNumber = stopDoc.getLong("stopNumber")?.toInt() ?: 0,
                                    name = stopDoc.getString("name") ?: "",
                                    arrivalTime = stopDoc.getString("arrivalTime") ?: "",
                                    coordinates = stopDoc.getGeoPoint("coordinates")?.let {
                                        LatLng(it.latitude, it.longitude)
                                    } ?: LatLng(0.0, 0.0)
                                )
                            }

                            val route = Route(
                                routeNumber = routeNumber,
                                name = routeName,
                                stops = stops
                            )
                            routes.add(route)
                            processedRoutes++

                            if (processedRoutes == totalRoutes) {
                                onSuccess(routes)
                            }
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
