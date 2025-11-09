package com.example.campuswheels.feature_home.presentation.home

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campuswheels.core.domain.models.UniversityBus
import com.example.campuswheels.core.presentation.components.CustomLoadingIndicator
import com.example.campuswheels.core.presentation.components.RefreshContainer
import com.example.campuswheels.core.util.DateTimeUtil.formatTime
import com.example.campuswheels.feature_bus_stop.domain.model.BusStopWithRoutes
import com.example.campuswheels.feature_bus_stop.presentation.components.BusStopTile
import com.example.campuswheels.feature_home.presentation.components.BusInfoDialog
import com.example.campuswheels.feature_home.presentation.components.BusTile
import com.example.campuswheels.ui.theme.NavyBlue300
import com.example.campuswheels.ui.theme.Red400
import com.example.campuswheels.ui.theme.White
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel : HomeViewModel = hiltViewModel(),
    snackbarState : SnackbarHostState = remember {
        SnackbarHostState()
    },
    onNearbyBusClick: (String) -> Unit,
    onNearbyBusStopClick: (String) -> Unit,
    onAllBusStopsClick: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(28.6139, 77.2090), 14f)
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(
        key1 = homeViewModel.uiState.errorNearbyBuses,
        key2 = homeViewModel.uiState.errorNearbyStops,
        key3 = homeViewModel.uiState.errorLocation
    ){
        Log.d("BTLogger","showSnackbar")
        if(homeViewModel.uiState.errorLocation!=null){
            snackbarState.showSnackbar(homeViewModel.uiState.errorLocation!!)
        }else if(homeViewModel.uiState.errorNearbyBuses!=null){
            snackbarState.showSnackbar(homeViewModel.uiState.errorNearbyBuses!!)
        }else if(homeViewModel.uiState.errorNearbyStops!=null){
            snackbarState.showSnackbar(homeViewModel.uiState.errorNearbyStops!!)
        }
    }

    val buses by homeViewModel.universityBuses.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Campus Wheels",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
            ) {
                if(homeViewModel.uiState.errorNearbyBuses!=null || homeViewModel.uiState.errorNearbyStops!=null) {
                    Snackbar(
                        snackbarData = it,
                        containerColor = Red400,
                        contentColor = White,
                    )
                } else {
                    Snackbar(snackbarData = it)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column {
                ShowMap(
                    cameraPositionState = cameraPositionState,
                    deviceLocation = homeViewModel.locationState,
                    buses = buses
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    "Track Buses",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                BusesList(
                    modifier = Modifier.weight(1f),
                    buses = buses,
                    isLoading = homeViewModel.isLoading,
                    onBusClick = { clickedBusId ->
                        val clickedBus = buses.find { it.id == clickedBusId }
                        clickedBus?.currentLocation?.let { loc ->
                            val position = LatLng(loc.latitude!!, loc.longitude!!)
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(position, 17f))
                            }
                        }
                    }
                )
//                NearbyBusesList(
//                    modifier = Modifier.weight(1f),
//                    buses = {homeViewModel.uiState.nearbyBuses},
//                    isLoading =  homeViewModel.uiState.isLoadingLocation || homeViewModel.uiState.isLoadingNearbyBuses,
////                    onRefresh = homeViewModel::getNearbyBuses,
//                    onBusClick = onNearbyBusClick
//                )
//                Spacer(
//                    modifier = Modifier.height(24.dp)
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        "Nearby Bus Stops",
//                        style = MaterialTheme.typography.titleSmall
//                    )
//                    Text(
//                        "All Bus Stops",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Blue500,
//                        modifier = Modifier
//                            .clickable {
//                                onAllBusStopsClick()
//                            }
//                            .padding(
//                                start = 4.dp, top = 4.dp, end = 4.dp, bottom = 2.dp
//                            )
//                    )
//                }
//                NearbyBusStopsList(
//                    modifier = Modifier.weight(3f),
//                    busStops = {homeViewModel.uiState.nearbyBusStops},
//                    isLoading = homeViewModel.uiState.isLoadingLocation || homeViewModel.uiState.isLoadingNearbyStops,
//                    isRefreshing = homeViewModel.uiState.isRefreshingNearbyStops,
////                    onRefresh = homeViewModel::getNearbyStops,
//                    onBusStopClick = onNearbyBusStopClick
//                )
//                ShareLocation(homeViewModel)
            }
        }
    }
}

@Composable
fun BusesList(
    modifier: Modifier = Modifier,
    buses : List<UniversityBus>,
    isLoading : Boolean,
//    onRefresh : (isLoading : Boolean, isRefreshing : Boolean) -> Unit,
    onBusClick : (String) -> Unit
) {
    if(isLoading) {
        return CustomLoadingIndicator(
            modifier = modifier
        )
    }
    if(buses.isEmpty()) {
        return RefreshContainer(
            modifier = Modifier.fillMaxHeight(0.3f),
            message = "No Nearby Buses Found!",
//            onRefresh = {
//                onRefresh(false, true)
//            }
        )
    }

    var selectedBus by remember { mutableStateOf<UniversityBus?>(null) }

    LazyColumn(
        content = {
            items(buses) {
                BusTile(
                    routeNo = it.route.routeNumber,
                    routeName = it.route.name,
                    vehNo = it.vehicleNumber,
                    onClick = {
                        onBusClick(it.id)
                    },
                    onInfoClick = { selectedBus = it }
                )
                Spacer(modifier = Modifier.width(14.dp))
            }
        },
        contentPadding = PaddingValues(8.dp),
    )

    selectedBus?.let { bus ->
        BusInfoDialog(bus = bus, onDismiss = { selectedBus = null })
    }
}

@Composable
fun NearbyBusStopsList(
    modifier: Modifier = Modifier,
    busStops : ()-> List<BusStopWithRoutes>,
    isLoading : Boolean,
    isRefreshing : Boolean,
//    onRefresh : (isLoading : Boolean, isRefreshing : Boolean)->Unit,
    onBusStopClick : (String)-> Unit
){
    if(isLoading){
        return CustomLoadingIndicator(
            modifier = modifier,
        )
    }
    if(busStops().isEmpty()){
        return RefreshContainer(
            modifier = Modifier.fillMaxHeight(0.4f),
            message = "No Nearby Bus Stops Found!",
//            onRefresh = {
//                onRefresh(false, true)
//            }
        )
    }
//    SwipeRefresh(
//        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
//        onRefresh = { onRefresh(false, true) },
//    ) {
        LazyColumn(
            content = {
                items(busStops()){
                    BusStopTile(
                        stopNo = it.stopNo,
                        stopName = it.name,
                        onClick = {
                            onBusStopClick(it.stopNo)
                        }
                    )
                    Divider(
                        color = NavyBlue300,
                    )
                }
            },
            contentPadding = PaddingValues(8.dp)
        )
//    }
}

@Composable
fun ShowMap(
    cameraPositionState: CameraPositionState,
    deviceLocation: Location?,
    buses: List<UniversityBus>
) {
//    val context = LocalContext.current
    var isMapLoaded by remember { mutableStateOf(false) }
    var isMapExpanded by remember { mutableStateOf(false) } // Not using as of now
    val deviceLocationMarkerState = deviceLocation?.let {
        rememberUpdatedMarkerState(
            position = LatLng(it.latitude, it.longitude)
        )
    }

//    val myLocationIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
//
//    LaunchedEffect(Unit) {
//        MapsInitializer.initialize(context)
//    }

    LaunchedEffect(isMapLoaded) {
        if (isMapLoaded) {
            deviceLocation?.let {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 10f),
                    durationMs = 1000
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isMapExpanded) 500.dp else 250.dp)
            .clip(RoundedCornerShape(16.dp))
//            .clickable { isMapExpanded = !isMapExpanded }
            .padding(8.dp)
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true }
        ) {
            deviceLocationMarkerState?.let {
                Marker(
                    state = it,
                    title = "Your Location",
//                    icon = myLocationIcon
                )
            }
            buses.forEach { bus ->
                val location = bus.currentLocation
                if (location != null && location.latitude != null && location.longitude != null) {
                    val markerState = rememberUpdatedMarkerState(
                        position = LatLng(location.latitude, location.longitude)
                    )
                    Marker(
                        state = markerState,
                        title = "Bus: route-${bus.route.routeNumber}",
                        snippet = "Last updated: ${formatTime(location.timestamp)}"
                    )
                }
            }
        }
    }
}
