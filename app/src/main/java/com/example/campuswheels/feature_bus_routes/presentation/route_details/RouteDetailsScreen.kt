package com.example.campuswheels.feature_bus_routes.presentation.route_details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campuswheels.R
import com.example.campuswheels.core.domain.models.Bus
import com.example.campuswheels.core.domain.models.Route
import com.example.campuswheels.core.presentation.components.CustomLoadingIndicator
import com.example.campuswheels.core.presentation.components.CustomOutlinedButton
import com.example.campuswheels.core.presentation.components.CustomTimeline
import com.example.campuswheels.core.presentation.components.FieldValue
import com.example.campuswheels.core.presentation.components.TimelineItem
import com.example.campuswheels.core.util.BusRouteUtil
import com.example.campuswheels.feature_bus_routes.domain.models.BusRouteWithStops
import com.example.campuswheels.feature_bus_routes.presentation.bus_routes.BusRoutesViewModel
import com.example.campuswheels.feature_bus_routes.presentation.components.RouteMapView
import com.example.campuswheels.feature_bus_routes.presentation.components.RouteScheduleBottomSheet
import com.example.campuswheels.ui.theme.Blue500
import com.example.campuswheels.ui.theme.Red400
import com.example.campuswheels.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailsScreen(
    routeNo: String,
    busRoutesViewModel: BusRoutesViewModel = hiltViewModel(),
    routeDetailsViewModel: RouteDetailsViewModel = hiltViewModel(),
    snackbarState : SnackbarHostState = remember {
        SnackbarHostState()
    },
) {
    Log.d("RouteBug", "RouteDetailsScreen: I got Route No.: $routeNo")
    LaunchedEffect(key1 = Unit) {
//        routeDetailsViewModel.getBusRouteDetails(routeNo,isLoading = true)
//        routeDetailsViewModel.connectSocket(routeNo)
        Log.d("RouteBug", "RouteDetailsScreen: Total Routes: ${busRoutesViewModel.busRoutesState.size}")
        busRoutesViewModel.busRoutesState.forEach {
            Log.d("RouteBug", "Route in list: '${it.routeNumber}' — comparing with '$routeNo'")
        }

    }

    LaunchedEffect(key1 = routeDetailsViewModel.uiState.error){
        if(routeDetailsViewModel.uiState.error!=null){
            snackbarState.showSnackbar(routeDetailsViewModel.uiState.error!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Route Details",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
            ) {
                if(routeDetailsViewModel.uiState.error!=null){
                    Snackbar(
                        snackbarData = it,
                        containerColor = Red400,
                        contentColor = White,

                    )
                } else {
                    Snackbar(snackbarData = it)
                }
            }
        },

        ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            BusRouteDetailsContainer(
                isLoading = routeDetailsViewModel.uiState.isLoading,
                isRefreshing = { routeDetailsViewModel.uiState.isRefreshing },
                busRoute = busRoutesViewModel.busRoutesState.find {
                    Log.d("RouteBug", "RouteDetailsScreen: Checking routeNo ($routeNo) with current routeNumber (${it.routeNumber})")
                    it.routeNumber == routeNo
                                                                  },
//                onRefresh = routeDetailsViewModel::getBusRouteDetails,
                liveBuses = { routeDetailsViewModel.uiState.buses },
                onClickSchedule = routeDetailsViewModel::toggleScheduleBottomSheet
            )
        }
    }

    if(routeDetailsViewModel.uiState.showScheduleBottomSheet && routeDetailsViewModel.uiState.route!=null){
        RouteScheduleBottomSheet(
            onDismissRequest = routeDetailsViewModel::toggleScheduleBottomSheet,
            route = {routeDetailsViewModel.uiState.route!!}
        )
    }
}


@Composable
fun BusRouteDetailsContainer(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isRefreshing: () -> Boolean,
//    onRefresh : (routeNo : String ,isLoading : Boolean,isRefreshing : Boolean)->Unit,
    busRoute: Route?,
    liveBuses: () -> List<Bus>,
    onClickSchedule: () -> Unit
) {
    if(isLoading) {
        return CustomLoadingIndicator()
    }

    if(busRoute == null) {
        return Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Something Went Wrong!")
        }
    }

    val scrollState = rememberScrollState()
//    val nextArrival by remember {
//        mutableStateOf(BusRouteUtil.getNextArrival(busRoute))
//    }

    return Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
//            RouteMapView(
//                busStops = busRoute.stops,
//                liveBuses = liveBuses()
//            )
            RouteMapView(
                stopsCoordinates = busRoute.stops.map { it.coordinates }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.53f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    start = 14.dp, end = 14.dp, top = 12.dp
                )
        ) {
            Column(
                modifier =  modifier
                        .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                FieldValue(field = "Route No", value = busRoute.routeNumber )
                Spacer(modifier = Modifier.height(6.dp))
                FieldValue(field = "Route Name", value = busRoute.name )
//                Spacer(modifier = Modifier.height(12.dp))
//                CustomOutlinedButton(
//                    onClick = onClickSchedule
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
//                        contentDescription = null,
//                        tint = Blue500
//                    )
//                    Spacer(modifier = Modifier.width(6.dp))
//                    Text(
//                        text = "View Schedule",
//                        color = Blue500,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Bus Stops: ",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTimeline(
                    items = busRoute.stops.mapIndexed { _, stop ->
                        TimelineItem(
                            stop.name,
                            listOf(
                                "Stop No.: ${stop.stopNumber}",
//                                "Next Arrival:  ${nextArrival[i]}"
                                "Arrival Time: ${stop.arrivalTime}"
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
