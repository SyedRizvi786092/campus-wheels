package com.example.campuswheels.feature_bus_routes.presentation.bus_routes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campuswheels.R
import com.example.campuswheels.core.domain.models.Route
import com.example.campuswheels.core.presentation.components.CustomLoadingIndicator
import com.example.campuswheels.core.presentation.components.CustomTextField
import com.example.campuswheels.core.presentation.components.RefreshContainer
import com.example.campuswheels.core.util.LoggerUtil
import com.example.campuswheels.feature_bus_routes.domain.models.BusRouteWithStops
import com.example.campuswheels.feature_bus_routes.presentation.components.BusRouteTile
import com.example.campuswheels.ui.theme.NavyBlue300
import com.example.campuswheels.ui.theme.Red400
import com.example.campuswheels.ui.theme.White
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusRoutesScreen(
    busRoutesViewModel: BusRoutesViewModel = hiltViewModel(),
    snackbarState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    onRouteItemClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val logger = LoggerUtil(c = "BusRoutesScreen")
    LaunchedEffect(key1 = busRoutesViewModel.uiState.error) {
        logger.info("Show Snackbar")

        if(busRoutesViewModel.uiState.error!=null){
            snackbarState.showSnackbar(busRoutesViewModel.uiState.error!!)
        }
    }

    Scaffold(
        modifier = Modifier
            .clickable(
                // Remove Ripple Effect
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Bus Routes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
            ) {
                if(busRoutesViewModel.uiState.error!=null){
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
            BusRouteList(
                busRoutes = busRoutesViewModel.busRoutesState,
                searchInput = {busRoutesViewModel.uiState.searchInput},
                onSearchInputChange = busRoutesViewModel::onSearchInputChange,
                isLoading = {busRoutesViewModel.uiState.isLoading},
                isRefreshing = {busRoutesViewModel.uiState.isRefreshing},
//                onRefresh = busRoutesViewModel::getAllBusRoutes,
                onRouteItemClick = onRouteItemClick,
                onClearFocus = { focusManager.clearFocus() },
                onClearSearchClick = {
                    busRoutesViewModel.onSearchInputChange("")
                    focusManager.clearFocus()
                }
            )
        }
    }
}

@Composable
private fun BusRouteList(
    busRoutes: List<Route>,
    searchInput: () -> String,
    onSearchInputChange: (String) -> Unit,
    isLoading: () -> Boolean,
    isRefreshing: () -> Boolean,
//    onRefresh : (isLoading : Boolean, isRefreshing : Boolean)->Unit,
    onRouteItemClick: (String) -> Unit,
    onClearFocus: () -> Unit,
    onClearSearchClick: () -> Unit
) {
    if(isLoading()) {
        return CustomLoadingIndicator()
    }

    Column() {
        CustomTextField(
            value = searchInput,
            onValueChange = onSearchInputChange,
            hintText = "Search Route",
            labelText = "Search Route",
            onClearFocus = onClearFocus,
            modifier = Modifier.padding(
                start = 12.dp, end=12.dp, bottom = 12.dp
            ),
            radius = 100,
            leadingIcon = Icons.Default.Search,
            trailingIcon = if(searchInput().isNotEmpty())
                ImageVector.vectorResource(id = R.drawable.baseline_cancel_24)
                    else null,
            onTrailingIconClick = onClearSearchClick,
        )
        if(busRoutes.isEmpty())
            RefreshContainer(
                modifier = Modifier.fillMaxHeight(0.4f),
                message = "No Bus Routes Found!",
//                onRefresh = if(searchInput().isEmpty()) {
//                    { onRefresh(false, true) }
//                } else null
            )
        else
//            SwipeRefresh(
//                state = rememberSwipeRefreshState(isRefreshing = isRefreshing()),
//                onRefresh = {onRefresh(false, true)},
//            ) {
                LazyColumn(
                    content = {
                        itemsIndexed(busRoutes) { index, route ->
                            if(index==0) {
                                HorizontalDivider(color = NavyBlue300)
                            }
                            BusRouteTile(
                                routeNo = route.routeNumber,
                                stops = route.stops,
                                totalStops = route.stops.size,
                                onClick = {
                                    Log.d("RouteBug", "BusRouteList: Route No. ${route.routeNumber} bus clicked!")
                                    onRouteItemClick(route.routeNumber)
                                }
                            )
                            HorizontalDivider(color = NavyBlue300)
                        }
                    },
                    contentPadding = PaddingValues(8.dp)
                )
//            }
    }
}
